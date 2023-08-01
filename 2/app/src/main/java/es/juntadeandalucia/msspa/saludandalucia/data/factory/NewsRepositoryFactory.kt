package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.NewsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.NewsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NewsRepository

class NewsRepositoryFactory(
    private val newsRepositoryMockImpl: NewsRepositoryMockImpl,
    private val newsRepositoryNetworkImpl: NewsRepositoryNetworkImpl
) : BaseRepositoryFactory<NewsRepository>() {

    override fun create(strategy: Strategy): NewsRepository =
        when (strategy) {
            Strategy.NETWORK -> newsRepositoryNetworkImpl
            else -> newsRepositoryMockImpl
        }
}
