package es.inteco.conanmobile.domain.repository

import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import io.reactivex.rxjava3.core.Completable

import io.reactivex.rxjava3.core.Single

/**
 * Analysis repository
 *
 * @constructor Create empty Analysis repository
 */
interface AnalysisRepository {
    /**
     * Is last analysis
     *
     * @return
     */
    fun isLastAnalysis():Single<Boolean>

    /**
     * Get last analysis
     *
     * @return
     */
    fun getLastAnalysis(): Single<AnalysisResultEntity>

    /**
     * Save analysis
     *
     * @param analysis
     * @return
     */
    fun saveAnalysis(analysis: AnalysisResultEntity): Completable

    /**
     * Remove all analysis
     *
     * @return
     */
    fun removeAllAnalysis(): Completable

    /**
     * Remove analysis
     *
     * @param analysis
     * @return
     */
    fun removeAnalysis(analysis: AnalysisResultEntity): Completable
}