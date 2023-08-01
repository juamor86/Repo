package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

/**
 * Set first access use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Set first access use case
 */
class SetFirstAccessUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase<Void>() {

    override fun buildUseCase(params: Void?): Completable = preferencesRepositoryFactory.create(Strategy.PREFERENCES).setIsFirstAccess()
}