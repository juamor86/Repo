package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import io.reactivex.rxjava3.core.Single

/**
 * Get default analysis use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Get default analysis use case
 */
class GetDefaultAnalysisUseCase (private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SingleUseCase<Void, AnalysisEntity>() {

    override fun buildUseCase(params: Void?): Single<AnalysisEntity> =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDefaultAnalysis()
}