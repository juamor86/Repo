package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import io.reactivex.rxjava3.core.Completable

/**
 * Save default analysis use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Save default analysis use case
 */
class SaveDefaultAnalysisUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase<SaveDefaultAnalysisUseCase.Params>() {

    override fun buildUseCase(params: Params?): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveDefaultAnalysis(params!!.defaultAnalysis)

    data class Params(val defaultAnalysis: AnalysisEntity)
}