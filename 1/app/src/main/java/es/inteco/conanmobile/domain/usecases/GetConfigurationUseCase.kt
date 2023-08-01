package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.mappers.ConfigurationMapper
import io.reactivex.rxjava3.core.Single


/**
 * Get configuration use case
 *
 * @property preferencesRepositoryFactory
 * @property configurationRepositoryFactory
 * @constructor Create empty Get configuration use case
 */
class GetConfigurationUseCase(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val configurationRepositoryFactory: ConfigurationRepositoryFactory
) : SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>() {

    override fun buildUseCase(params: Params?): Single<ConfigurationEntity> =
        if (params?.key?.isNotEmpty() == true) {
            configurationRepositoryFactory.create(strategy = Strategy.NETWORK)
                .getConfiguration(params.key).map {
                ConfigurationMapper.convert(it)
            }

        } else {
            preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
                .flatMap { device ->
                    configurationRepositoryFactory.create(strategy = Strategy.PREFERENCES).run {
                        getConfiguration()
                    }
                }
        }

    data class Params(val key: String)
}