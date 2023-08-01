package es.juntadeandalucia.msspa.authentication.data.factory

import es.juntadeandalucia.msspa.authentication.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.repository.mock.LoginMockImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.network.LoginImpl
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.repository.LoginRepository

class LoginPersonalDataRepositoryFactory(
    private val loginPersonalDataMockImpl: LoginMockImpl,
    private val loginPersonalDataNetworkImpl: LoginImpl
) : BaseRepositoryFactory<LoginRepository>() {

    override fun create(strategy: Strategy): LoginRepository =
        when (strategy) {
            Strategy.MOCK -> loginPersonalDataMockImpl
            else -> loginPersonalDataNetworkImpl
        }
}
