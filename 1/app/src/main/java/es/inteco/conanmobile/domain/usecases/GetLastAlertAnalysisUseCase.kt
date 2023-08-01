package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SynchronousUseCase


/**
 * Get last alert analysis use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Get last alert analysis use case
 */
class GetLastAlertAnalysisUseCase(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory
) : SynchronousUseCase<Void, Long>() {

    override fun execute(params: Void?): Long {
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getLastAlertAnalysis()
    }
}