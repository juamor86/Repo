package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceRequestData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AdvicesRepository
import io.reactivex.Completable
import io.reactivex.Single

class AdvicesRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    AdvicesRepository {
    override fun getAdvices(nuhsa: String): Single<AdviceData> {
        return msspaApi.getAdvices(nuhsa = nuhsa)
    }

    override fun getAdvicesReceived(phone: String): Single<AdviceData> {
        return msspaApi.getAdvicesReceived(phone = phone)
    }

    override fun updateAdvice(id: String, advice: AdviceRequestData.Entry.Resource): Completable {
        return msspaApi.updateAdvice(id = id, AdviceRequestData = advice)
    }

    override fun removeAdvice(id: String): Completable {
        return msspaApi.removeAdvice(id = id)
    }

    override fun createAdvice(advice: AdviceRequestData): Completable {
        return msspaApi.createAdvice(AdviceRequestData = advice)
    }
}