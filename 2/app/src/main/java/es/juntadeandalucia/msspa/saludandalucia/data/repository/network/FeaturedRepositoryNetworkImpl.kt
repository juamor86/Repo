package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.FeaturedData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.FeaturedRepository
import io.reactivex.Single

class FeaturedRepositoryNetworkImpl(
    private val msspaApi: MSSPAApi
) : FeaturedRepository {

    override fun getFeatured(): Single<List<FeaturedData>> {
        val list = msspaApi.getFeatured(ApiConstants.NewsApi.TYPE_FEATURED)
        return list.map { it.entry }
    }
}
