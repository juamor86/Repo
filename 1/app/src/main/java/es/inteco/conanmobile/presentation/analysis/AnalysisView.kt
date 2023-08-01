package es.inteco.conanmobile.presentation.analysis

import es.inteco.conanmobile.domain.entities.AnalysisResultEntity

/**
 * Analysis view
 *
 * @constructor Create empty Analysis view
 */
interface AnalysisView {
    /**
     * Update progress
     *
     * @param progress
     */
    fun updateProgress(progress: Int)

    /**
     * Navigate to results
     *
     * @param result
     * @param previousAnalysis
     */
    fun navigateToResults(result: AnalysisResultEntity, previousAnalysis: AnalysisResultEntity?)

    /**
     * Set progress max
     *
     * @param max
     */
    fun setProgressMax(max: Int)

    /**
     * Set device warnings
     *
     * @param deviceWarnings
     */
    fun setDeviceWarnings(deviceWarnings: Int)

    /**
     * Set app warnings
     *
     * @param appWarnings
     */
    fun setAppWarnings(appWarnings: Int)

    /**
     * Set system warnings
     *
     * @param systemWarnings
     */
    fun setSystemWarnings(systemWarnings: Int)

    /**
     * Show alert no network
     *
     */
    fun showAlertNoNetwork()

    /**
     * Navigate back
     *
     */
    fun navigateBack()

    /**
     * Show info message
     *
     * @param message
     */
    fun showInfoMessage(message: String)

    /**
     * Send notification
     *
     */
    fun sendNotification()
}
