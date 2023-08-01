package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SynchronousUseCase


/**
 * Save last alert analysis use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Save last alert analysis use case
 */
class SaveLastAlertAnalysisUseCase(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory
) : SynchronousUseCase<Long, Unit>() {

    override fun execute(params: Long?) {
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveLastAlertAnalysis(params!!)
    }
}