package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceTypesData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AdviceTypesRepository
import io.reactivex.Single

class AdviceTypesRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
AdviceTypesRepository {
    override fun getAdviceTypes(): Single<AdviceTypesData> {
        return msspaApi.getAdviceTypes()
    }
}