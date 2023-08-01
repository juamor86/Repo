package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Meta
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.MetaEntity

class MetaMapper {
    companion object {
        fun convert(model: Meta): MetaEntity =
            MetaEntity(
                lastUpdated = model.lastUpdated,
            )

        fun convert(model: MetaEntity): Meta =
            Meta(
                lastUpdated = model.lastUpdated,
            )
    }
}