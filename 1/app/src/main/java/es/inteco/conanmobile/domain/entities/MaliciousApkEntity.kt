package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.parcel.Parcelize

/**
 * Malicious apk entity
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Malicious apk entity
 */
@Parcelize
class MaliciousApkEntity(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("status")
    val status: Long,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("message")
    val message: MaliciousApkMessageEntity,
    @SerializedName("path")
    val path: String
) : Parcelable

/**
 * Malicious apk message entity
 *
 * @property datas
 * @constructor Create empty Malicious apk message entity
 */
@Parcelize
data class MaliciousApkMessageEntity(
    @SerializedName("datas")
    val datas: List<SAnalysis>
) : Parcelable

/**
 * S analysis
 *
 * @property data
 * @property type
 * @property services
 * @constructor Create empty S analysis
 */
@Parcelize
data class SAnalysis(
    @SerializedName("data")
    val data: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("services")
    val services: List<ServiceEntity>
) : Parcelable

/**
 * Malicious analysis
 *
 * @property data
 * @property services
 * @constructor Create empty Malicious analysis
 */
@Parcelize
data class MaliciousAnalysis(
    @SerializedName("data")
    val data: String,
    @SerializedName("services")
    val services: List<ServiceEntity>
) : Parcelable

/**
 * Service entity
 *
 * @property service
 * @property result
 * @property analysisDate
 * @constructor Create empty Service entity
 */
@Parcelize
data class ServiceEntity(
    @SerializedName("service")
    val service: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("analysisDate")
    val analysisDate: String
) : Parcelable {
    companion object {
        enum class MaliciousResultType {
            MALWARE,
            UNKNOWN,
            OTHERS
        }
    }

    val maliciousType: MaliciousResultType
        get() = when (result) {
            Consts.MALICIOUS_MALWARE -> MaliciousResultType.MALWARE
            Consts.MALICIOUS_UNKNOWN -> MaliciousResultType.UNKNOWN
            else -> MaliciousResultType.OTHERS
        }

}