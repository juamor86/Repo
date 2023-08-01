package es.inteco.conanmobile.data.repository.network

import es.inteco.conanmobile.data.api.CONANApi
import es.inteco.conanmobile.data.entities.ConfigurationData
import es.inteco.conanmobile.data.entities.RegisterDeviceRequestData
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.repository.ConfigurationRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Configuration repository network impl
 *
 * @property conanApi
 * @constructor Create empty Configuration repository network impl
 */
class ConfigurationRepositoryNetworkImpl(private val conanApi: CONANApi) :
    ConfigurationRepository {

    override fun getConfiguration(key: String): Single<ConfigurationData> {
        return conanApi.getConfiguration(key)
    }

    override fun registerDevice(body: RegisterDeviceRequestData): Single<RegisteredDeviceData> {
        return conanApi.registerDevice(body)
    }

    override fun getConfiguration(): Single<ConfigurationEntity> {
        TODO("Not yet implemented")
    }

    override fun saveConfiguration(configuration: ConfigurationEntity): Completable {
        TODO("Not yet implemented")
    }
}