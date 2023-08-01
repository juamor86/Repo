package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.FeaturedRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.FeaturedRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.FeaturedRepository

class FeaturedRepositoryFactory(
    private val featuredRepositoryMockImpl: FeaturedRepositoryMockImpl,
    private val featuredRepositoryNetworkImpl: FeaturedRepositoryNetworkImpl
) : BaseRepositoryFactory<FeaturedRepository>() {

    override fun create(strategy: Strategy): FeaturedRepository =
        when (strategy) {
            Strategy.NETWORK -> featuredRepositoryNetworkImpl
            else -> featuredRepositoryMockImpl
        }
}
