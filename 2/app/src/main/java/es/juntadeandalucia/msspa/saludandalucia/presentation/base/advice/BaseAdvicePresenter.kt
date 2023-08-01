package es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_STATUS
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.MOBILE
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import timber.log.Timber


abstract class BaseAdvicePresenter<V : BaseAdviceContract.View> :  BaseAdviceContract.Presenter {

    protected lateinit var view: V

    override fun setViewContract(baseFragment: BaseContract.View) {
        view = baseFragment as V
    }

    protected var adviceType: AdviceTypeResource? = null
    protected var entryAdvice: EntryAdviceEntity? = null
    protected var advice: AdviceEntity? = null
    protected var nuhsa: String = ""
    protected var sharedContacts: MutableList<ValueReferenceEntity> = mutableListOf()
    protected var isNewAdvice: Boolean = false
    protected var isSaving: Boolean = false
    protected var byApp: Boolean = false
    protected var byEmail: Boolean = false
    protected var bySMS: Boolean = false
    protected var phoneNumber: String = ""
    protected var email: String = ""

    override fun onViewCreated() {
        sharedContacts.clear()
        super.onViewCreated()
        getScreenNameTracking()?.apply { view.sendEvent(this) }
        view.setupCommonListeners()
    }

    open fun getScreenNameTracking(): String? = null

    override fun saveArguments(
        nuhsa: String,
        adviceType: AdviceTypeResource?,
        entryAdvice: EntryAdviceEntity?,
        phoneNumber: String?,
        advice: AdviceEntity?
    ) {
        this.nuhsa = nuhsa
        this.adviceType = adviceType
        this.entryAdvice = entryAdvice
        this.phoneNumber = phoneNumber ?: ""
        this.advice = advice
    }
//TODO reset contacts
    /*override fun extractContactFromIntentCursor(contentResolver: ContentResolver, uri: Uri) {
        var number = ""
        var name = ""
        val numbersContact: MutableList<ContactAdviceEntity> = mutableListOf()
        try {
            contentResolver.query(uri, null, null, null, null)?.let { cursor ->
                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    // Get all phone numbers.
                    contentResolver.query(
                        Phone.CONTENT_URI, null,
                        Phone.CONTACT_ID + " = " + contactId, null, null)?.let { phones ->
                        while (phones.moveToNext()) {
                            number = phones.getString(phones.getColumnIndex(Phone.NUMBER))
                            val type = phones.getInt(phones.getColumnIndex(Phone.TYPE))
                            val label  = extractLabelFromPhone(type, phones)
                            numbersContact.add(
                                ContactAdviceEntity(
                                    name = name,
                                    number = Utils.phoneFormatted(number, false),
                                    type = type,
                                    label = label
                                )
                            )
                        }
                        phones.close()
                    }
                }
                cursor.close()
            }
            view.performExtractContact(numberContactUnified(numbersContact))
        } catch (e: Exception) {
            Timber.e("Error getting contact: ${e.message}")
        }
    }*/

   /* private fun extractLabelFromPhone(
        type: Int,
        phones: Cursor
    ): String {
        return try {
            if (type == TYPE_CUSTOM) phones.getString(phones.getColumnIndex(Phone.LABEL)) else ""
        } catch (e: Exception) {
            ""
        }
    }

    override fun buildExtractContact(numbersContact: List<ContactAdviceEntity>) {
        if (!numbersContact.isNullOrEmpty()) {
            if (numbersContact.size == 1) {
                importContact(numbersContact.get(0))
            } else {
                view.showImportNumbersContact(numbersContact)
            }
        }
    }

    override fun onNumberContactItemClick(numberContact: ContactAdviceEntity) {
        view.dismissDialogImportContact()
        importContact(numberContact)
    }

    private fun importContact(contactAdviceEntity: ContactAdviceEntity) {
        val number = Utils.phoneFormatted(contactAdviceEntity.number)
        if (Utils.phoneFormatted(phoneNumber) == number) {
            view.showDialogContactSelf()
            return
        }

        if (!Utils.checkPhoneIsCorrect(number)) {
            view.showErrorPhoneNotValid()
            return
        }

        val newContact = ValueReferenceEntity(
            id = number,
            type = "Subscription",
            display = contactAdviceEntity.number,
            name = contactAdviceEntity.name
        )

        if (existContactList(newContact)) {
            view.showErrorContactAlreadyAdded(newContact.id)
        } else {
            sharedContacts.add(newContact)
            sharedContacts.let { view.loadContactsToRecyclerView(it) }
        }
    }*/

    override fun onAdviceItemClick(contact: ValueReferenceEntity, itemView: View) {
    }

    override fun onAdviceItemRemoved(contact: ValueReferenceEntity) {
    }

    override fun onRemoveButtonClick(context: Context, contact: ValueReferenceEntity) {
        Utils.getContactByPhoneNumber(context, contact.display).let { display ->
            contact.display = display ?: contact.display
        }

        advice?.entry?.get(0)?.text?.let { title ->
            view.showRemoveContactDialog(contact, title)
        }

        adviceType?.text?.let { title ->
            view.showRemoveContactDialog(contact, title)
        }
    }

    override fun removeContact(contact: ValueReferenceEntity) {
        sharedContacts.remove(contact)
        sharedContacts.let { view.loadContactsToRecyclerView(it) }
    }

    protected fun isOwner(item: EntryAdviceEntity?): Boolean {
        var owner = false

        if (item?.extension?.size == 1) {
            owner = true
        } else {
            item?.extension?.forEach { extensionEntity ->
                if (extensionEntity.url == Consts.SHARED_SUBSCRIPTION) {
                    owner = true
                }
            }
        }

        return owner
    }

    protected fun sharedWith(item: EntryAdviceEntity): MutableList<ValueReferenceEntity> {
        val sharedUsers = mutableListOf<ValueReferenceEntity>()

        item.extension.forEach { extensionEntity ->
            if (extensionEntity.url == Consts.SHARED_SUBSCRIPTION) {
                sharedUsers.add(extensionEntity.valueReference)
            }
        }

        return sharedUsers
    }

    protected fun sharedBy(item: EntryAdviceEntity): String {
        var shared = ""

        item.extension.forEach { extensionEntity ->
            if (extensionEntity.url == Consts.PARENT_SUBSCRIPTION) {
                shared = extensionEntity.valueReference.display
            }
        }

        return shared
    }

    protected fun getContactData(): MutableList<AdviceContactEntity> {
        val contact = mutableListOf<AdviceContactEntity>()

        if (byApp) {
            contact.add(AdviceContactEntity(system = ContactTypesEntity.PHONE.name.lowercase(), value = phoneNumber, use = MOBILE, extension = listOf(ContactExtensionEntity(url = ADVICE_STATUS, valueCode = AdvicesStatus.ACTIVE.name.lowercase()))))
        } else {
            contact.add(AdviceContactEntity(system = ContactTypesEntity.PHONE.name.lowercase(), value = phoneNumber, use = MOBILE, extension = listOf(ContactExtensionEntity(url = ADVICE_STATUS, valueCode = Consts.ADVICE_TYPE_OFF))))
        }

        if (byEmail) {
            contact.add(AdviceContactEntity(system = ContactTypesEntity.EMAIL.name.lowercase() , value = email, use = MOBILE, extension = listOf(ContactExtensionEntity(url = ADVICE_STATUS, valueCode = AdvicesStatus.ACTIVE.name.lowercase()))))
        }

        if (bySMS) {
            contact.add(AdviceContactEntity(system = ContactTypesEntity.SMS.name.lowercase(), value = phoneNumber, use = MOBILE, extension = listOf(ContactExtensionEntity(url = ADVICE_STATUS, valueCode = AdvicesStatus.ACTIVE.name.lowercase()))))
        }
        
        return contact
    }

    override fun switchItemChanged(enabledEmail: Boolean) {
        view.showOrHideEmailEditText(enabledEmail)
    }

    override fun checkEmailFormat(email: String) {
        if (Utils.isEmailValid(email)) view.showValidEmail() else view.showInvalidEmail()
    }

    override fun onRequestPermissionContactDenied() {
        view.showDialogPermissionContactsDeniedTwice()
    }
//TODO reset contacts
    /*private fun existContactList(valueReferenceEntity: ValueReferenceEntity): Boolean {
        var existContactList = false
        sharedContacts.forEach { contact ->
            if (Utils.phoneFormatted(contact.display) == valueReferenceEntity.id) {
                existContactList = true
            }
        }
        return existContactList
    }

    private fun numberContactUnified(numbersContact: MutableList<ContactAdviceEntity>): MutableList<ContactAdviceEntity> {
        val numContactUnified: MutableList<ContactAdviceEntity> = mutableListOf()
        numbersContact.forEach { contactAdviceEntity ->
            if (!numContactUnified.contains(contactAdviceEntity)) {
                numContactUnified.add(contactAdviceEntity)
            }
        }
        return numContactUnified
    }*/
}