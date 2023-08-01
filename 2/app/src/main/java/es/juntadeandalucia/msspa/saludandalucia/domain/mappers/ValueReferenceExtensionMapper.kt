package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.ValueReferenceExtension
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceExtensionEntity

class ValueReferenceExtensionMapper {
    companion object {

        fun convert(valueReferences: List<ValueReferenceExtension>?) = valueReferences?.map { convert(it) }

        fun convert(model: ValueReferenceExtension): ValueReferenceExtensionEntity =
            ValueReferenceExtensionEntity(
                url = model.url,
                valueCode = model.valueCode
            )

        @JvmName("convert1")
        fun convert(valueReferences: List<ValueReferenceExtensionEntity>?) = valueReferences?.map { convert(it) }

        fun convert(model: ValueReferenceExtensionEntity): ValueReferenceExtension =
            ValueReferenceExtension(
                url = model.url,
                valueCode = model.valueCode
            )
    }
}