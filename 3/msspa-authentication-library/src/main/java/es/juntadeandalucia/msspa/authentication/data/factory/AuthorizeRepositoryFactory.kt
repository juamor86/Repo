package es.juntadeandalucia.msspa.authentication.data.factory

import es.juntadeandalucia.msspa.authentication.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.repository.mock.AuthorizeRepositoryMockImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.network.AuthorizeRepositoryNetworkImpl
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.repository.AuthorizeRepository

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
