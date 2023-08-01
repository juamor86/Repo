package es.inteco.conanmobile.domain.repository

import es.inteco.conanmobile.data.entities.*
import io.reactivex.rxjava3.core.Single

/**
 * Incibe repository
 *
 * @constructor Create empty Incibe repository
 */
interface IncibeRepository {
    /**
     * Get ip botnet
     *
     * @return
     */
    fun getIpBotnet(): Single<IpBotnetData>

    /**
     * Get warnings
     *
     * @param key
     * @return
     */
    fun getWarnings(key: String): Single<WarningsData>

    /**
     * Get o s i tips
     *
     * @param key
     * @return
     */
    fun getOSITips(key: String): Single<OSIData>

    /**
     * Is malicious app
     *
     * @param key
     * @param body
     * @return
     */
    fun isMaliciousApp(key: String, body: MaliciousAppRequestData): Single<MaliciousAppData>

    /**
     * Post analysis result
     *
     * @param key
     * @param body
     * @return
     */
    fun postAnalysisResult(
        key: String,
        body: AnalysisResultRequestData
    ): Single<PostAnalysisResultData>

    /**
     * Post malicious apk
     *
     * @param key
     * @param body
     * @return
     */
    fun postMaliciousApk(key: String, body: MaliciousApkRequestData): Single<MaliciousApkData>

    /**
     * Get pending warnings
     *
     * @param key
     * @return
     */
    fun getPendingWarnings(key: String): Single<PendingWarningsData>
}