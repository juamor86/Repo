package es.inteco.conanmobile.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Malicious apk data
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Malicious apk data
 */
class MaliciousApkData(
    val timestamp: String,
    val status: Long,
    val statusMessage: String,
    val message: MaliciousApkMessage,
    val path: String
)

/**
 * Malicious apk message
 *
 * @property data
 * @constructor Create empty Malicious apk message
 */
data class MaliciousApkMessage(
    @SerializedName("datas")
    val data: List<ApkData>
)

/**
 * Apk data
 *
 * @property data
 * @property type
 * @property services
 * @constructor Create empty Apk data
 */
data class ApkData(
    val data: String,
    val type: String,
    val services: List<ApkService>
)

/**
 * Other apk data
 *
 * @property data
 * @property services
 * @constructor Create empty Other apk data
 */
data class OtherApkData(
    val data: String,
    val services: List<Service>
)

/**
 * Apk service
 *
 * @property service
 * @property result
 * @property analysisDate
 * @constructor Create empty Apk service
 */
data class ApkService(
    val service: String,
    val result: String,
    val analysisDate: String
)