package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

/**
 * Set analysis launched use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Set analysis launched use case
 */
class SetAnalysisLaunchedUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase<SetAnalysisLaunchedUseCase.Params>() {

    override fun buildUseCase(params: Params?): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).setDefaultAnalysisLaunched(params!!.analysisLaunched)

    data class Params(val analysisLaunched: Boolean)
}
