package es.inteco.conanmobile.data.factory

import es.inteco.conanmobile.data.factory.base.BaseRepositoryFactory
import es.inteco.conanmobile.data.repository.mock.PreferencesRepositoryMockImpl
import es.inteco.conanmobile.data.repository.preferences.PreferencesRepositoryPreferencesImpl
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.repository.PreferencesRepository

/**
 * Preferences repository factory
 *
 * @property preferencesRepositoryMockImpl
 * @property preferencesRepositoryPreferencesImpl
 * @constructor Create empty Preferences repository factory
 */
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