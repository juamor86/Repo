package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.EntryExtension
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryExtensionEntity

class EntryExtensionMapper {
    companion object {

        fun convert(entryExtensions: List<EntryExtension>) = entryExtensions.mapNotNull { convert(it) }

        fun convert(model: EntryExtension): EntryExtensionEntity =
                EntryExtensionEntity(
                    url = model.url,
                    valueReference = ValueReferenceMapper.convert(model.valueReference)
                )

        @JvmName("convert1")
        fun convert(entryExtensions: List<EntryExtensionEntity>) = entryExtensions.mapNotNull { convert(it) }

        fun convert(model: EntryExtensionEntity): EntryExtension =
            EntryExtension(
                url = model.url,
                valueReference = ValueReferenceMapper.convert(model.valueReference)
            )
    }
}