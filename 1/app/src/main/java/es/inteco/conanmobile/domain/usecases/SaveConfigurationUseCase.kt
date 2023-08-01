package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import io.reactivex.rxjava3.core.Completable

/**
 * Save configuration use case
 *
 * @property configurationRepositoryFactory
 * @constructor Create empty Save configuration use case
 */
class SaveConfigurationUseCase(private val configurationRepositoryFactory: ConfigurationRepositoryFactory) :
    CompletableUseCase<SaveConfigurationUseCase.Params>() {

    override fun buildUseCase(params: Params?): Completable =
        configurationRepositoryFactory.create(Strategy.PREFERENCES).saveConfiguration(params!!.configuration)

    /**
     * Params
     *
     * @param configuration
     */
    data class Params(val configuration: ConfigurationEntity)

}