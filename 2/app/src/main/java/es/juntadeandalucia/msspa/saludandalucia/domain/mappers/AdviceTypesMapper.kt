package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceTypeEntryData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceTypesData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*

class AdviceTypesMapper {

    companion object {
        fun convert(adviceTypeData: AdviceTypesData) = AdviceTypeEntity(
            id = adviceTypeData.id,
            resourceType = adviceTypeData.resourceType,
            meta = AdviceTypeMeta(lastUpdated = adviceTypeData.meta.lastUpdated),
            type = adviceTypeData.type,
            total = adviceTypeData.total,
            link = AdviceTypeLinkMapper.convert(adviceTypeData.link),
            entry = convertEntry(adviceTypeData.entry)
        )

        fun convertEntry(notifications: List<AdviceTypeEntryData>) = notifications.map { convert(it) }

        fun convert(model: AdviceTypeEntryData) =
            AdviceTypeEntry(
                AdviceTypeResource(id = model.resource.id,
                        resourceType = model.resource.resourceType,
                        text = model.resource.text,
                        status = model.resource.status,
                        reason = model.resource.reason,
                        criteria = model.resource.criteria,
                        channel = AdviceTypeChannel(type = model.resource.channel.type)
                )
            )
    }
}