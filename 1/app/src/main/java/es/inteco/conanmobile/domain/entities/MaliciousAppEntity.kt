package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Malicious app entity
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Malicious app entity
 */
@Parcelize
class MaliciousAppEntity(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("status")
    val status: Long,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("message")
    val message: MaliciousAppMessageEntity,
    @SerializedName("path")
    val path: String
) : Parcelable

/**
 * Malicious app message entity
 *
 * @property hash
 * @property services
 * @constructor Create empty Malicious app message entity
 */
@Parcelize
data class MaliciousAppMessageEntity(
    @SerializedName("hash")
    val hash: String,
    @SerializedName("services")
    val services: @RawValue List<ServiceEntity>
) : Parcelable