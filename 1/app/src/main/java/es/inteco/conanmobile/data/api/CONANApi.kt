package es.inteco.conanmobile.data.api

import es.inteco.conanmobile.BuildConfig
import es.inteco.conanmobile.data.entities.*
import es.inteco.conanmobile.data.utils.ApiConstants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*

/**
 * C o n a n api
 *
 * @constructor Create empty C o n a n api
 */
interface CONANApi {
    companion object {
        private const val URL_REGISTER_DEVICE =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/device/new"
        private const val URL_CONFIGURATION =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/configuration"
        private const val URL_WARNINGS =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/safetynotifications"
        private const val URL_OSI_TIPS =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/ositips"
        private const val URL_MALICIOUS_APP =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/malware"
        private const val URL_ANALYSIS_RESULT =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/device/analysis"
        private const val URL_MALICIOUS_APK =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/malicious"
        private const val URL_PENDING_WARNINGS =
            "${BuildConfig.API_BASE_HOST}/${ApiConstants.General.URL_API_V1}/safetynotifications/haveNotifications"
    }

    /**
     * Register device
     *
     * @param body
     * @return
     */
    @POST(URL_REGISTER_DEVICE)
    fun registerDevice(
        @Body body: RegisterDeviceRequestData
    ): Single<RegisteredDeviceData>

    /**
     * Get configuration
     *
     * @param key
     * @param language
     * @return
     */
    @GET(URL_CONFIGURATION)
    fun getConfiguration(
        @Header(ApiConstants.Header.KEY_HEADER) key: String,
        @Header(ApiConstants.Header.LANGUAGE_HEADER) language: String = Locale.getDefault().language.lowercase()
    ): Single<ConfigurationData>

    /**
     * Get ip botnet
     *
     * @param token
     * @return
     */
    @GET(ApiConstants.General.BOTNET_URL)
    fun getIpBotnet(
        @Header(ApiConstants.Incibe.X_INTECO_WS_REQUEST_SOURCE) token: String = ApiConstants.Incibe.API,
    ): Single<IpBotnetData>

    /**
     * Get warnings
     *
     * @param key
     * @param language
     * @return
     */
    @GET(URL_WARNINGS)
    fun getWarnings(
        @Header(ApiConstants.Header.KEY_HEADER) key: String,
        @Header(ApiConstants.Header.LANGUAGE_HEADER) language: String = Locale.getDefault().language.lowercase()
    ): Single<WarningsData>

    /**
     * Get osi tips
     *
     * @param key
     * @param language
     * @return
     */
    @GET(URL_OSI_TIPS)
    fun getOsiTips(
        @Header(ApiConstants.Header.KEY_HEADER) key: String,
        @Header(ApiConstants.Header.LANGUAGE_HEADER) language: String = Locale.getDefault().language.lowercase()
    ): Single<OSIData>

    /**
     * Is malicious app
     *
     * @param key
     * @param body
     * @return
     */
    @POST(URL_MALICIOUS_APP)
    fun isMaliciousApp(
        @Header(ApiConstants.Header.KEY_HEADER) key: String,
        @Body body: MaliciousAppRequestData
    ): Single<MaliciousAppData>

    /**
     * Post malicious apk
     *
     * @param key
     * @param body
     * @return
     */
    @POST(URL_MALICIOUS_APK)
    fun postMaliciousApk(
        @Header(ApiConstants.Header.KEY_HEADER) key: String,
        @Body body: MaliciousApkRequestData
    ): Single<MaliciousApkData>

    /**
     * Post analysis result
     *
     * @param key
     * @param body
     * @return
     */
    @POST(URL_ANALYSIS_RESULT)
    fun postAnalysisResult(
        @Header(ApiConstants.Header.KEY_HEADER) key: String,
        @Body body: AnalysisResultRequestData
    ): Single<PostAnalysisResultData>

    /**
     * Get pending warnings
     *
     * @param key
     * @return
     */
    @GET(URL_PENDING_WARNINGS)
    fun getPendingWarnings(
        @Header(ApiConstants.Header.KEY_HEADER) key: String): Single<PendingWarningsData>
}
