package es.juntadeandalucia.msspa.authentication.data.factory

import es.juntadeandalucia.msspa.authentication.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.repository.mock.PreferencesRepositoryMockImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.persistence.PreferencesRepositoryPreferencesImpl
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.repository.PreferencesRepository

class PreferencesRepositoryFactory(
        private val preferencesRepositoryMockImpl: PreferencesRepositoryMockImpl,
        private val preferencesRepositoryPreferencesImpl: PreferencesRepositoryPreferencesImpl
) : BaseRepositoryFactory<PreferencesRepository>() {

    override fun create(strategy: Strategy): PreferencesRepository =
            when (strategy) {
                Strategy.PREFERENCES -> preferencesRepositoryPreferencesImpl
                else -> preferencesRepositoryMockImpl
            }
}
