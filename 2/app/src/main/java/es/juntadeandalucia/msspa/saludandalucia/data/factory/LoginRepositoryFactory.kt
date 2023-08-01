package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.LoginRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.LoginRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.LoginRepository

class LoginRepositoryFactory(
    private val loginRepositoryMockImpl: LoginRepositoryMockImpl,
    private val loginRepositoryNetworkImpl: LoginRepositoryNetworkImpl
) : BaseRepositoryFactory<LoginRepository>() {

    override fun create(strategy: Strategy): LoginRepository =
        when (strategy) {
            Strategy.NETWORK -> loginRepositoryNetworkImpl
            else -> loginRepositoryMockImpl
        }
}
