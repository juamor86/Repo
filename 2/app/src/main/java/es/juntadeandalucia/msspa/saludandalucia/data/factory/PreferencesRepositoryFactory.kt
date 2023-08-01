package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.PreferencesRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.PreferencesRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.preferences.PreferencesRepositoryPreferencesImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.PreferencesRepository

class PreferencesRepositoryFactory(
    private val preferencesRepositoryMockImpl: PreferencesRepositoryMockImpl,
    private val preferencesRepositoryPreferencesImpl: PreferencesRepositoryPreferencesImpl,
    private val preferencesRepositoryNetworkImpl: PreferencesRepositoryNetworkImpl
) : BaseRepositoryFactory<PreferencesRepository>() {

    override fun create(strategy: Strategy): PreferencesRepository =
        when (strategy) {
            Strategy.PREFERENCES -> preferencesRepositoryPreferencesImpl
            Strategy.NETWORK -> preferencesRepositoryNetworkImpl
            else -> preferencesRepositoryMockImpl
        }
}
