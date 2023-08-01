package es.inteco.conanmobile.domain.repository

import es.inteco.conanmobile.data.entities.OSIData
import es.inteco.conanmobile.data.entities.WarningsData
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Preferences repository
 *
 * @constructor Create empty Preferences repository
 */
interface PreferencesRepository {
    /**
     * Get first access
     *
     * @return
     */
    fun getFirstAccess(): Boolean

    /**
     * Set is first access
     *
     * @return
     */
    fun setIsFirstAccess(): Completable

    /**
     * Save default analysis
     *
     * @param defaultAnalysis
     * @return
     */
    fun saveDefaultAnalysis(defaultAnalysis: AnalysisEntity): Completable

    /**
     * Get default analysis
     *
     * @return
     */
    fun getDefaultAnalysis(): Single<AnalysisEntity>

    /**
     * Set default analysis launched
     *
     * @param launched
     * @return
     */
    fun setDefaultAnalysisLaunched(launched: Boolean): Completable

    /**
     * Get default analysis launched
     *
     * @return
     */
    fun getDefaultAnalysisLaunched(): Boolean

    /**
     * Set next available analysis date time
     *
     * @param datetime
     * @return
     */
    fun setNextAvailableAnalysisDateTime(datetime: Long): Completable

    /**
     * Get next available analysis date time
     *
     * @return
     */
    fun getNextAvailableAnalysisDateTime(): Long

    /**
     * Get first analysis launched
     *
     * @return
     */
    fun getFirstAnalysisLaunched(): Boolean

    /**
     * Set is first analysis launched
     *
     * @return
     */
    fun setIsFirstAnalysisLaunched(): Completable

    /**
     * Save device register
     *
     * @param device
     * @return
     */
    fun saveDeviceRegister(device: RegisteredDeviceEntity): Completable

    /**
     * Get device register
     *
     * @return
     */
    fun getDeviceRegister(): Single<RegisteredDeviceEntity>

    /**
     * Save osi tips
     *
     * @param osiTips
     * @return
     */
    fun saveOsiTips(osiTips: OSIData): Completable

    /**
     * Get osi tips
     *
     * @return
     */
    fun getOsiTips(): Single<OSIData>

    /**
     * Exist osi tips
     *
     * @return
     */
    fun existOsiTips():Boolean

    /**
     * Save warnings
     *
     * @param osiTips
     * @return
     */
    fun saveWarnings(osiTips: WarningsData): Completable

    /**
     * Get warnings
     *
     * @return
     */
    fun getWarnings(): Single<WarningsData>

    /**
     * Exist warnings
     *
     * @return
     */
    fun existWarnings():Boolean

    /**
     * Get last alert analysis
     *
     * @return
     */
    fun getLastAlertAnalysis():Long

    /**
     * Save last alert analysis
     *
     * @param time
     */
    fun saveLastAlertAnalysis(time: Long)
}