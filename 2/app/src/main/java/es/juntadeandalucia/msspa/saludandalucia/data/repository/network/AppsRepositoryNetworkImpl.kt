package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AppData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AppsRepository
import io.reactivex.Single

class AppsRepositoryNetworkImpl(private val msspaApi: MSSPAApi) : AppsRepository {

    override fun getApps(device: String): Single<List<AppData>> = msspaApi.getApps(device).map {
        it.entry
    }
}
