package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AdviceTypesRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AdviceTypesRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AdviceTypesRepository

class AdviceTypesRepositoryFactory(
        private val adviceTypesRepositoryMockImpl: AdviceTypesRepositoryMockImpl,
        private val adviceTypesRepositoryNetworkImpl: AdviceTypesRepositoryNetworkImpl
) : BaseRepositoryFactory<AdviceTypesRepository>() {

    override fun create(strategy: Strategy): AdviceTypesRepository =
            when (strategy) {
                Strategy.NETWORK -> adviceTypesRepositoryNetworkImpl
                else -> adviceTypesRepositoryMockImpl
            }
}