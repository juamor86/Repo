package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

/**
 * Set next analysis date time use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Set next analysis date time use case
 */
class SetNextAnalysisDateTimeUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase<SetNextAnalysisDateTimeUseCase.Params>() {

    override fun buildUseCase(params: Params?): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).setNextAvailableAnalysisDateTime(params!!.nextDatetime)

    data class Params(val nextDatetime: Long = -1)
}