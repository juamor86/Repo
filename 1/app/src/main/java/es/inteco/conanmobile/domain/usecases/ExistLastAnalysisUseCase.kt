package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase

/**
 * Exist last analysis use case
 *
 * @property analysisRepositoryFactory
 * @constructor Create empty Exist last analysis use case
 */
class ExistLastAnalysisUseCase(private val analysisRepositoryFactory: AnalysisRepositoryFactory) :
    SingleUseCase<Void, Boolean>() {

    override fun buildUseCase(params: Void?) =
        analysisRepositoryFactory.create(Strategy.PREFERENCES).isLastAnalysis()
}