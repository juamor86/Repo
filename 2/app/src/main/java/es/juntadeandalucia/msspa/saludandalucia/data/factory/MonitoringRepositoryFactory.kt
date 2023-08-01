package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.MonitoringRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.MonitoringRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.MonitoringRepository

class MonitoringRepositoryFactory(
    private val followUpMockImpl: MonitoringRepositoryMockImpl,
    private val followUpNetwork: MonitoringRepositoryNetworkImpl
) : BaseRepositoryFactory<MonitoringRepository>() {

    override fun create(strategy: Strategy): MonitoringRepository =
        when (strategy) {
            Strategy.NETWORK -> followUpNetwork
            else -> followUpMockImpl
        }
}
