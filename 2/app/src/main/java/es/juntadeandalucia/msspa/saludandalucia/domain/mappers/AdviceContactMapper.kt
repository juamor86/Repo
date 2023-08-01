package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Contact
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity

class AdviceContactMapper {
    companion object {

        fun convert(contact: List<Contact>?) = contact?.map { convert(it) }

        fun convert(model: Contact): AdviceContactEntity =
            AdviceContactEntity(
                system = model.system,
                value = model.value,
                use = model.use,
                extension = ContactExtensionMapper.convertModelListToEntityList(model.extension)
            )

        @JvmName("convert1")
        fun convert(contact: List<AdviceContactEntity>?) = contact?.map { convert(it) }

        fun convert(model: AdviceContactEntity): Contact =
            Contact(
                system = model.system,
                value = model.value,
                use = model.use,
                extension = ContactExtensionMapper.convertEntityListToModelList(model.extension)
            )
    }
}