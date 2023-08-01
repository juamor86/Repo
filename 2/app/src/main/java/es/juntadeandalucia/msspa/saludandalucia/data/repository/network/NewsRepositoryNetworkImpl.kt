package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NewsData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NewsRepository
import io.reactivex.Single

class NewsRepositoryNetworkImpl(
    private val msspaApi: MSSPAApi
) : NewsRepository {

    override fun getNews(): Single<List<NewsData>> {
        val list = msspaApi.getNews(ApiConstants.NewsApi.TYPE_CONTENT)
        return list.map { it.entry }
    }
}
