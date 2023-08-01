package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.ValueReference
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity

class ValueReferenceMapper {
    companion object {
        fun convert(model: ValueReference): ValueReferenceEntity =
            ValueReferenceEntity(
                type = model.type,
                id = model.id,
                display = model.display,
                extension = ValueReferenceExtensionMapper.convert(model.extension)
            )

        fun convert(model: ValueReferenceEntity): ValueReference =
            ValueReference(
                type = model.type,
                id = model.id,
                display = model.display,
                extension = ValueReferenceExtensionMapper.convert(model.extension)
            )
    }
}