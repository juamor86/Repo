package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.file.DynamicUIRepositoryFileImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.DynamicUIRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.DynamicUIRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicUIRepository

class DynamicUIRepositoryFactory(
    private val dynamicUIRepositoryMockImpl: DynamicUIRepositoryMockImpl,
    private val dynamicUIRepositoryNetworkImpl: DynamicUIRepositoryNetworkImpl,
    private val dynamicUIRepositoryFileImpl: DynamicUIRepositoryFileImpl
) : BaseRepositoryFactory<DynamicUIRepository>() {

    override fun create(strategy: Strategy): DynamicUIRepository =
        when (strategy) {
            Strategy.NETWORK -> dynamicUIRepositoryNetworkImpl
            Strategy.FILE -> dynamicUIRepositoryFileImpl
            else -> dynamicUIRepositoryMockImpl
        }
}
