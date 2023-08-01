package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AdvicesRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AdvicesRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AdvicesRepository

class AdvicesRepositoryFactory(
        private val advicesRepositoryMockImpl: AdvicesRepositoryMockImpl,
        private val advicesRepositoryNetworkImpl: AdvicesRepositoryNetworkImpl
) : BaseRepositoryFactory<AdvicesRepository>() {

    override fun create(strategy: Strategy): AdvicesRepository =
            when (strategy) {
                Strategy.NETWORK -> advicesRepositoryNetworkImpl
                else -> advicesRepositoryMockImpl
            }
}