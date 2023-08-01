package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Link
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LinkEntity

class LinkMapper {
    companion object {

        fun convert(valueReferences: List<Link>) = valueReferences?.map { convert(it) }

        fun convert(model: Link): LinkEntity =
            LinkEntity(
                relation = model.relation,
                url = model.url
            )
    }
}