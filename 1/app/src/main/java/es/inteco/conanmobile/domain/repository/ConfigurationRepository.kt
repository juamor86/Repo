package es.inteco.conanmobile.domain.repository

import es.inteco.conanmobile.data.entities.ConfigurationData
import es.inteco.conanmobile.data.entities.RegisterDeviceRequestData
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Configuration repository
 *
 * @constructor Create empty Configuration repository
 */
interface ConfigurationRepository {
    /**
     * Register device
     *
     * @param body
     * @return
     */
    fun registerDevice(body: RegisterDeviceRequestData): Single<RegisteredDeviceData>

    /**
     * Get configuration
     *
     * @param key
     * @return
     */
    fun getConfiguration(key: String): Single<ConfigurationData>

    /**
     * Save configuration
     *
     * @param configuration
     * @return
     */
    fun saveConfiguration(configuration: ConfigurationEntity): Completable

    /**
     * Get configuration
     *
     * @return
     */
    fun getConfiguration(): Single<ConfigurationEntity>
}