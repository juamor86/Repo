package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AppointmentsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase

class CancelAppointmentUseCase(private val appointmentsRepositoryFactory: AppointmentsRepositoryFactory) :
    CompletableUseCase() {

    override fun buildUseCase() = appointmentsRepositoryFactory.create(Strategy.NETWORK).run {
        cancelAppointment()
    }
}
