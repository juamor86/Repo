package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.UserRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.UserRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.preferences.UserRepositoryPreferencesImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.UserRepository

class UserRepositoryFactory(
    private val userRepositoryMockImpl: UserRepositoryMockImpl,
    private val userRepositoryPreferencesImpl: UserRepositoryPreferencesImpl,
    private val userRepositoryNetworkImpl: UserRepositoryNetworkImpl
) : BaseRepositoryFactory<UserRepository>() {

    override fun create(strategy: Strategy): UserRepository =
        when (strategy) {
            Strategy.NETWORK -> userRepositoryNetworkImpl
            Strategy.PREFERENCES -> userRepositoryPreferencesImpl
            else -> userRepositoryMockImpl
        }
}
