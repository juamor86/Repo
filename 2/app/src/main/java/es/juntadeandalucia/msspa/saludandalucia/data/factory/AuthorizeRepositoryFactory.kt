package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AuthorizeRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AuthorizeRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AuthorizeRepository

class AuthorizeRepositoryFactory(
    private val authorizeRepositoryMockImpl: AuthorizeRepositoryMockImpl,
    private val authorizeRepositoryNetworkImpl: AuthorizeRepositoryNetworkImpl
) : BaseRepositoryFactory<AuthorizeRepository>() {

    override fun create(strategy: Strategy): AuthorizeRepository =
        when (strategy) {
            Strategy.NETWORK -> authorizeRepositoryNetworkImpl
            else -> authorizeRepositoryMockImpl
        }
}
