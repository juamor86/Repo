package es.inteco.conanmobile.data.repository.mock

import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.repository.AnalysisRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Analysis repository mock impl
 *
 * @constructor Create empty Analysis repository mock impl
 */
class AnalysisRepositoryMockImpl :
    AnalysisRepository {

    override fun isLastAnalysis(): Single<Boolean> = Single.just(false)

    override fun getLastAnalysis(): Single<AnalysisResultEntity> = Single.just(
        AnalysisResultEntity(
            AnalysisEntity("")
        )
    )

    override fun saveAnalysis(analysis: AnalysisResultEntity): Completable = Completable.complete()

    override fun removeAllAnalysis(): Completable = Completable.complete()

    override fun removeAnalysis(analysis: AnalysisResultEntity): Completable =
        Completable.complete()
}