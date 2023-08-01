package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import io.reactivex.Completable

interface AppointmentsRepository {

    fun cancelAppointment(): Completable
}
