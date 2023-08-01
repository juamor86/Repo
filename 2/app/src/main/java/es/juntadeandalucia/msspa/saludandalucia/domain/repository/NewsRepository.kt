package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NewsData
import io.reactivex.Single

interface NewsRepository {

    fun getNews(): Single<List<NewsData>>
}
