package es.juntadeandalucia.msspa.saludandalucia.data.entities.news

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NewsData

data class MSSPANewsResponseData (
    var total: Int,
    var entry: List<NewsData>
)