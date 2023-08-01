package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.entities.RegisterDeviceRequestData
import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import es.inteco.conanmobile.domain.mappers.RegisteredDeviceMapper
import io.reactivex.rxjava3.core.Single

/**
 * Register device use case
 *
 * @property configurationRepositoryFactory
 * @constructor Create empty Register device use case
 */
class RegisterDeviceUseCase(
    private val configurationRepositoryFactory: ConfigurationRepositoryFactory
) : SingleUseCase<RegisterDeviceUseCase.Params, RegisteredDeviceEntity>() {

    override fun buildUseCase(params: Params?): Single<RegisteredDeviceEntity> {
        return configurationRepositoryFactory.create(strategy = Strategy.NETWORK)
            .registerDevice(
                RegisterDeviceRequestData(params!!.idDevice)
            ).map {
                RegisteredDeviceMapper.convert(it)
            }
    }

    data class Params(val idDevice: String)
}