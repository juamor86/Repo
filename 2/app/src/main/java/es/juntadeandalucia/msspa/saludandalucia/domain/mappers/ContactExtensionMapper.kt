package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.ContactExtension
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactExtensionEntity

class ContactExtensionMapper {
    companion object {
        fun convert(model: ContactExtension) = with(model) {
            ContactExtensionEntity(url = url, valueCode = valueCode)
        }
        fun convert(entity: ContactExtensionEntity) = with(entity) {
            ContactExtension(url = url, valueCode = valueCode)
        }

        fun convertModelListToEntityList(modelList:List<ContactExtension>) = modelList.map { convert(it) }

        fun convertEntityListToModelList(entityList:List<ContactExtensionEntity>) = entityList.map { convert(it) }
    }
}