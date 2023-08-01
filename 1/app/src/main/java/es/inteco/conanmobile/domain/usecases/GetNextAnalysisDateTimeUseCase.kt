package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SynchronousUseCase

/**
 * Get next analysis date time use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Get next analysis date time use case
 */
class GetNextAnalysisDateTimeUseCase (private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Void, Long>() {

    override fun execute(params: Void?): Long =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getNextAvailableAnalysisDateTime()
}