package es.inteco.conanmobile.domain.repository

import es.inteco.conanmobile.data.entities.DefaultAnalysisListData
import io.reactivex.rxjava3.core.Single

/**
 * Default analysis repository
 *
 * @constructor Create empty Default analysis repository
 */
interface DefaultAnalysisRepository {
    /**
     * Get analysis
     *
     * @return
     */
    fun getAnalysis(): Single<DefaultAnalysisListData>
}