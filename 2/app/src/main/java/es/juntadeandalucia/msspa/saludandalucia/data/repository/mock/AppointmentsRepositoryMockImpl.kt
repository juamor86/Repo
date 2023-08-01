package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AppointmentsRepository
import io.reactivex.Completable

class AppointmentsRepositoryMockImpl() : AppointmentsRepository {
    override fun cancelAppointment() = Completable.complete()
}
