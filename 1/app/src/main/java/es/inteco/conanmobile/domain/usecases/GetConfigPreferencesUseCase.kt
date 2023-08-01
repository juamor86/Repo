package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import io.reactivex.rxjava3.core.Single

/**
 * Get config preferences use case
 *
 * @property configurationRepositoryFactory
 * @constructor Create empty Get config preferences use case
 */
class GetConfigPreferencesUseCase(private val configurationRepositoryFactory: ConfigurationRepositoryFactory) :
    SingleUseCase<Void, ConfigurationEntity>() {

    override fun buildUseCase(params: Void?): Single<ConfigurationEntity> {
        return configurationRepositoryFactory.create(Strategy.PREFERENCES).getConfiguration()
    }
}