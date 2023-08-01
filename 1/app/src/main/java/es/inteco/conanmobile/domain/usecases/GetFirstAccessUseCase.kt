package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SynchronousUseCase

/**
 * Get first access use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Get first access use case
 */
class GetFirstAccessUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Void, Boolean>() {

    override fun execute(params: Void?): Boolean =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getFirstAccess()
}