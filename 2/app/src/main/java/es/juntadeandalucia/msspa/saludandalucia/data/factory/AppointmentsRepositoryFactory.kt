package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AppointmentsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AppointmentsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AppointmentsRepository

class AppointmentsRepositoryFactory(
    private val appointmentsRepositoryMockImpl: AppointmentsRepositoryMockImpl,
    private val appointmentsRepositoryNetworkImpl: AppointmentsRepositoryNetworkImpl
) : BaseRepositoryFactory<AppointmentsRepository>() {

    override fun create(strategy: Strategy): AppointmentsRepository =
        when (strategy) {
            Strategy.NETWORK -> appointmentsRepositoryNetworkImpl
            else -> appointmentsRepositoryMockImpl
        }
}
