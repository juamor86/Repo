package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.advice

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceRequestData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.Advices.EVENT
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.Advices.NUHSA
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.Advices.POST
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.Advices.SUBSCRIPTION
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.Advices.TYPE_URL_SUB_FATHER
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.Advices.TYPE_URL_SUB_SHARED
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdvicesStatus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactExtensionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactTypesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_STATUS
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.FULL_URL_CONSTS
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils

class AdviceRequestMapper {

    companion object {

        private lateinit var  adviceCreateData : AdviceCreateData

        fun convertToRequest(
            phone:String,
            nuhsa: String,
            contact: MutableList<AdviceContactEntity>,
            sharedContacts: MutableList<ValueReferenceEntity>,
            model: AdviceTypeResource,
            type: String,
            contactEmpty:Boolean = false
        ) : AdviceRequestData {
            buildAdviceCreateData(phone, sharedContacts).let { adviceCreateData ->
                this.adviceCreateData = adviceCreateData
            }
            val entryList = mutableListOf<AdviceRequestData.Entry>()
            val entryFather = convertEntryToRequest(adviceCreateData.id,nuhsa, contact, sharedContacts, model, TYPE_URL_SUB_SHARED,contactEmpty)
            entryList.add(entryFather)
            entryList.addAll(convertEntryToRequestSharedContacts(nuhsa, contact, sharedContacts, model, TYPE_URL_SUB_FATHER))
            return AdviceRequestData(
                resourceType = "Bundle",
                type = type,
                total = entryList.size,
                entry = entryList
            )
        }

        private fun convertEntryToRequestSharedContacts(nuhsa: String, contact: MutableList<AdviceContactEntity>, sharedContacts: MutableList<ValueReferenceEntity>, model: AdviceTypeResource, typeUrlSub:String):MutableList<AdviceRequestData.Entry>{
            val entryList = mutableListOf<AdviceRequestData.Entry>()
            sharedContacts.forEach  { sharedContact->
                val listSharedContact = mutableListOf<ValueReferenceEntity>()
                listSharedContact.add(sharedContact)
                val idChildren = adviceCreateData.idChildrenMap[sharedContact.id]!!
                val entryChildren = convertEntryToRequest(idChildren, nuhsa, contact, listSharedContact, model, typeUrlSub)
                entryList.add(entryChildren)
            }
            return entryList
        }

        private fun convertEntryToRequest(id: String, nuhsa: String, contact: MutableList<AdviceContactEntity>, sharedContacts:  MutableList<ValueReferenceEntity>, model: AdviceTypeResource, typeUrlSub:String, contactEmpty:Boolean = false) : AdviceRequestData.Entry {
            return AdviceRequestData.Entry(
                    fullUrl = "$FULL_URL_CONSTS/${id}",
                    request = AdviceRequestData.Entry.Request(POST, FULL_URL_CONSTS),
                    resource = convertResource(nuhsa, contact, sharedContacts, model, typeUrlSub,contactEmpty)
            )
        }

        private fun convertResource(nuhsa: String, contact: MutableList<AdviceContactEntity>, sharedContacts: MutableList<ValueReferenceEntity>, model: AdviceTypeResource, typeUrlSub:String, contactEmpty:Boolean = false): AdviceRequestData.Entry.Resource {
            return AdviceRequestData.Entry.Resource(
                id = adviceCreateData.id,
                channel = AdviceRequestData.Entry.Resource.Channel(type = model.channel.type),
                contact = convertContactToRequest(createListContact(contact, sharedContacts, typeUrlSub)),
                criteria = model.criteria.replace(NUHSA, nuhsa),
                extension = getNewExtension(model, sharedContacts, typeUrlSub),
                reason = model.text,
                resourceType = model.resourceType,
                status = AdvicesStatus.REQUESTED.name.lowercase(),
                text = model.text,
            )
        }

        private fun getNewExtension(model: AdviceTypeResource, sharedUsers: List<ValueReferenceEntity>, typeUrlSub:String): List<AdviceRequestData.Entry.Resource.Extension> {
            val list =  mutableListOf(
                AdviceRequestData.Entry.Resource.Extension(
                    url = EVENT,
                    AdviceRequestData.Entry.Resource.Extension.ValueReference(
                        type = SUBSCRIPTION,
                        id = adviceCreateData.id,
                        display = model.text
                    )
                )
            )
            convertSharedUsersCreate(sharedUsers, list, typeUrlSub, model)
            return list
        }

        fun convertSharedUsersCreate(usersList: List<ValueReferenceEntity>, extensionList : MutableList<AdviceRequestData.Entry.Resource.Extension>, typeUrlSub: String, model: AdviceTypeResource) {
            usersList.forEach { user ->
                extensionList.add(
                    AdviceRequestData.Entry.Resource.Extension(
                        url = typeUrlSub,
                        AdviceRequestData.Entry.Resource.Extension.ValueReference(
                            type = SUBSCRIPTION, id = if (typeUrlSub.equals(TYPE_URL_SUB_FATHER)) adviceCreateData.id else adviceCreateData.idChildrenMap[user.id]!!,
                            display = if (typeUrlSub.equals(TYPE_URL_SUB_FATHER)) adviceCreateData.phone else user.id)
                    )
                )
            }
        }

        private fun convertContactToRequest( list: List<AdviceContactEntity>?) : List<AdviceRequestData.Entry.Resource.Contact> {
            return  if (list.isNullOrEmpty()) {
                emptyList()
            } else {
                list.map { convertContact(it) }
            }
        }

        private fun convertContact(contact: AdviceContactEntity) = AdviceRequestData.Entry.Resource.Contact(
            system = contact.system,
            use = contact.use,
            value = contact.value,
            extension = contact.extension.map { AdviceRequestData.Entry.Resource.Extension(url = "status", valueCode = it.valueCode) }
        )

        fun convertResource(
            nuhsa: String,
            contact: List<AdviceContactEntity>,
            sharedContacts: MutableList<ValueReferenceEntity>,
            model: EntryAdviceEntity,
            id: String? = null,
            status: String,
            isDelegate: Boolean = false
        ): AdviceRequestData.Entry.Resource =
            AdviceRequestData.Entry.Resource(
                id = id,
                channel = AdviceRequestData.Entry.Resource.Channel(type = model.channel.type),
                contact = convertContactToRequest(contact),
                criteria = model.criteria.replace(NUHSA, nuhsa),
                extension = if(isDelegate) AdviceRequestExtensionMapper.convertList(model.extension) else getNewExtension(model, sharedContacts),
                reason = model.extension.find { it.url == "evento" }?.valueReference?.display ?: model.reason,
                resourceType = model.resourceType,
                status = status,
                text = model.text,
            )

        private fun getNewExtension(model: EntryAdviceEntity, sharedUsers: List<ValueReferenceEntity>): List<AdviceRequestData.Entry.Resource.Extension> {
            val list =  mutableListOf(
                AdviceRequestData.Entry.Resource.Extension(
                    url = EVENT,
                    AdviceRequestData.Entry.Resource.Extension.ValueReference(
                        type = SUBSCRIPTION, id = model.id, display = model.text)
                )
            )

            //convertSharedUsers(model.id, sharedUsers, list)

            return list
        }

        fun convertSharedUsers(id: String, usersList: List<ValueReferenceEntity>, extensionList : MutableList<AdviceRequestData.Entry.Resource.Extension>) {
            usersList.forEach { user ->
                extensionList.add(
                    AdviceRequestData.Entry.Resource.Extension(
                        url = TYPE_URL_SUB_SHARED,
                        AdviceRequestData.Entry.Resource.Extension.ValueReference(
                            type = SUBSCRIPTION, id = id, display = user.id)
                    )
                )
            }
        }

        private fun createListContact(contact: MutableList<AdviceContactEntity>, sharedContacts: MutableList<ValueReferenceEntity>, typeUrlSub: String): MutableList<AdviceContactEntity>{
            return if (typeUrlSub == TYPE_URL_SUB_FATHER){
                mutableListOf(AdviceContactEntity(system = "phone" , use = "mobile", value = sharedContacts[0].id, extension = listOf(ContactExtensionEntity("status", AdvicesStatus.REQUESTED.name.lowercase()))))
            }else{
                contact
            }
        }

        private fun buildAdviceCreateData(phone: String, sharedContacts: MutableList<ValueReferenceEntity>): AdviceRequestMapper.Companion.AdviceCreateData {
            val idChildrenMap: MutableMap<String, String> = HashMap()
            sharedContacts.forEach {
                idChildrenMap[it.id] = Utils.getUniqueAdviceId()
            }
            return AdviceCreateData(phone = phone, id = Utils.getUniqueAdviceId(), idChildrenMap = idChildrenMap)
        }

        data class AdviceCreateData (
            val phone: String,
            val id: String,
            val idChildrenMap: MutableMap<String, String> = HashMap()
        )

        fun createChildrenRequestObject(
            father: EntryAdviceEntity,
            contact: ValueReferenceEntity,
            nuhsa:String,
            phoneNumber:String
        ): AdviceRequestData {
            return AdviceRequestData(
                resourceType = "Bundle",
                type = "message",
                total = 1,
                entry = listOf(convertEntryToRequestChildren(father, contact,nuhsa,phoneNumber))
            )
        }

        private fun convertEntryToRequestChildren(
            father: EntryAdviceEntity,
            contact: ValueReferenceEntity,
            nuhsa: String,
            phoneNumber:String
        ) : AdviceRequestData.Entry {
            return AdviceRequestData.Entry(
                fullUrl = "$FULL_URL_CONSTS/${father.id}",
                request = AdviceRequestData.Entry.Request(POST, FULL_URL_CONSTS),
                resource = convertResourceChildren(father, contact,nuhsa,phoneNumber)
            )
        }

        private fun convertResourceChildren(
            father: EntryAdviceEntity,
            contact: ValueReferenceEntity,
            nuhsa: String,
            phoneNumber:String
        ): AdviceRequestData.Entry.Resource {
            return AdviceRequestData.Entry.Resource(
                channel = AdviceRequestData.Entry.Resource.Channel(
                    type = father.channel.type
                ),
                contact = createChildrenContact(contact),
                criteria = father.criteria.replace(NUHSA, nuhsa),
                extension = createChildrenExtension(father, phoneNumber),
                reason = father.reason,
                resourceType = father.resourceType,
                status = AdvicesStatus.REQUESTED.name.lowercase(),
                text = father.text,
            )
        }

        private fun createChildrenContact(contact: ValueReferenceEntity): List<AdviceRequestData.Entry.Resource.Contact> {
            return listOf(
                AdviceRequestData.Entry.Resource.Contact(
                    system = ContactTypesEntity.PHONE.name.lowercase(),
                    use = Consts.MOBILE,
                    value = Utils.phoneFormatted(contact.display),
                    extension = listOf(
                        AdviceRequestData.Entry.Resource.Extension(
                            url = ADVICE_STATUS,
                            valueCode = AdvicesStatus.REQUESTED.name.lowercase(),
                        )
                    )
                )
            )
        }

        private fun createChildrenExtension(
            father: EntryAdviceEntity,
            phoneNumber: String
        ): List<AdviceRequestData.Entry.Resource.Extension> {
            val extensionList: MutableList<AdviceRequestData.Entry.Resource.Extension> = mutableListOf()
            father.extension[0].let{extension->
                extensionList.add(
                    AdviceRequestData.Entry.Resource.Extension(
                    url = EVENT,
                    AdviceRequestData.Entry.Resource.Extension.ValueReference(
                        type = SUBSCRIPTION,
                        id = extension.valueReference.id,
                        display = extension.valueReference.display
                    )
                ))
            }

            extensionList.add(AdviceRequestData.Entry.Resource.Extension(
                url = TYPE_URL_SUB_FATHER,
                AdviceRequestData.Entry.Resource.Extension.ValueReference(
                    type = SUBSCRIPTION,
                    id = Utils.getUniqueAdviceId(),
                    display = Utils.phoneFormatted(phoneNumber)
                )))
            return extensionList
        }
    }
}