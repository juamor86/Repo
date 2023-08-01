package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Channel
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ChannelEntity

class ChannelMapper {
    companion object {
        fun convert(model: Channel): ChannelEntity =
            ChannelEntity(
                type = model.type,
            )

        fun convert(model: ChannelEntity): Channel =
            Channel(
                type = model.type,
            )
    }
}