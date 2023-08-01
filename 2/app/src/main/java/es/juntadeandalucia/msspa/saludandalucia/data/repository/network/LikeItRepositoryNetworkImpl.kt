package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.LikeItResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.FeedbackRepository
import io.reactivex.Single

class LikeItRepositoryNetworkImpl(private val msspaApi: MSSPAApi) : FeedbackRepository {

    override fun getEvents(): Single<LikeItResponseData> = msspaApi.getLikeIt()


}