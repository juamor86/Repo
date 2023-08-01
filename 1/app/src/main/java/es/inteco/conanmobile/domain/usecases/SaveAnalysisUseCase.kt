package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import io.reactivex.rxjava3.core.Completable

/**
 * Save analysis use case
 *
 * @property analysisRepositoryFactory
 * @constructor Create empty Save analysis use case
 */
class SaveAnalysisUseCase(private val analysisRepositoryFactory: AnalysisRepositoryFactory) :
    CompletableUseCase<SaveAnalysisUseCase.Params>() {

    override fun buildUseCase(params: Params?): Completable =
        analysisRepositoryFactory.create(Strategy.PREFERENCES).saveAnalysis(params!!.analysisResult)

    data class Params(val analysisResult: AnalysisResultEntity)
}