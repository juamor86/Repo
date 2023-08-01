package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AppsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AppsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AppsRepository

class AppsRepositoryFactory(
    private val appsRepositoryMockImpl: AppsRepositoryMockImpl,
    private val appsRepositoryNetworkImpl: AppsRepositoryNetworkImpl
) : BaseRepositoryFactory<AppsRepository>() {

    override fun create(strategy: Strategy): AppsRepository =
        when (strategy) {
            Strategy.NETWORK -> appsRepositoryNetworkImpl
            else -> appsRepositoryMockImpl
        }
}
