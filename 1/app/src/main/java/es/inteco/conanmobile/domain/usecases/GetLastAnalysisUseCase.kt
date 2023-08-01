package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import javax.inject.Inject

/**
 * Get last analysis use case
 *
 * @property analysisRepositoryFactory
 * @constructor Create empty Get last analysis use case
 */
class GetLastAnalysisUseCase @Inject constructor(private val analysisRepositoryFactory: AnalysisRepositoryFactory) :
    SingleUseCase<Void, AnalysisResultEntity>() {

    override fun buildUseCase(params: Void?) =
        analysisRepositoryFactory.create(Strategy.PREFERENCES).getLastAnalysis()
}