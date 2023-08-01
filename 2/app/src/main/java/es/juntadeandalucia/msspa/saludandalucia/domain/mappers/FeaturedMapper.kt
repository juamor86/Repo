package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.FeaturedData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.FeaturedEntity

class FeaturedMapper {

    companion object {

        fun convert(featured: List<FeaturedData>) = featured.map { convert(it) }

        fun convert(model: FeaturedData) = FeaturedEntity(
            title = model.title,
            subtitle = model.alt,
            operationMode = model.operationMode,
            linkMode = model.linkMode,
            imgHeader = model.imgHeader
        )
    }
}
