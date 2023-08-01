package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.KeyValueData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.KeyValueEntity

class KeyValueMapper {

    companion object {

        fun convert(news: List<KeyValueData>) = news.map { convert(it) }

        fun convert(model: KeyValueData) = KeyValueEntity(
            key = model.key,
            value = model.value
        )
    }
}
