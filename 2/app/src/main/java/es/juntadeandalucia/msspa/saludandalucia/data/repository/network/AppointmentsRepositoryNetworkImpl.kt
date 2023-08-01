package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AppointmentsRepository
import io.reactivex.Completable

class AppointmentsRepositoryNetworkImpl(
    private val msspaApi: MSSPAApi
) : AppointmentsRepository {
    override fun cancelAppointment(): Completable {
        return msspaApi.cancelAppointment()
    }
}
