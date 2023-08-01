package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AppData
import io.reactivex.Single

interface AppsRepository {

    fun getApps(device: String): Single<List<AppData>>
}
