package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NewsData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity

class NewsMapper {

    companion object {

        fun convert(news: List<NewsData>) = news.map { convert(it) }

        fun convert(model: NewsData) = NewsEntity(
            title = model.title,
            subtitle = model.alt,
            description = model.description,
            url = model.linkMode,
            imgHeader = model.imgHeader,
            operationMode = model.operationMode
        )
    }
}
