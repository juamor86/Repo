package es.inteco.conanmobile.data.factory

import es.inteco.conanmobile.data.factory.base.BaseRepositoryFactory
import es.inteco.conanmobile.data.repository.mock.ConfigurationRepositoryMockImpl
import es.inteco.conanmobile.data.repository.network.ConfigurationRepositoryNetworkImpl
import es.inteco.conanmobile.data.repository.preferences.ConfigurationRepositoryPreferencesImpl
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.repository.ConfigurationRepository

/**
 * Configuration repository factory
 *
 * @property configurationRepositoryMockImpl
 * @property configurationRepositoryNetworkImpl
 * @property ConfigurationRepositoryPreferencesImpl
 * @constructor Create empty Configuration repository factory
 */
class ConfigurationRepositoryFactory(
    private val configurationRepositoryMockImpl: ConfigurationRepositoryMockImpl,
    private val configurationRepositoryNetworkImpl: ConfigurationRepositoryNetworkImpl,
    private val ConfigurationRepositoryPreferencesImpl: ConfigurationRepositoryPreferencesImpl
) : BaseRepositoryFactory<ConfigurationRepository>() {

    override fun create(strategy: Strategy): ConfigurationRepository =
        when (strategy) {
            Strategy.NETWORK -> configurationRepositoryNetworkImpl
            Strategy.PREFERENCES -> ConfigurationRepositoryPreferencesImpl
            else -> configurationRepositoryMockImpl
        }
}