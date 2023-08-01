package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceTypeLinkData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeLink

class AdviceTypeLinkMapper {
    companion object {

        fun convert(valueReferences: List<AdviceTypeLinkData>) = valueReferences.map { convert(it) }

        fun convert(model: AdviceTypeLinkData): AdviceTypeLink =
            AdviceTypeLink(
                relation = model.relation,
                url = model.url
            )
    }
}