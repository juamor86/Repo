package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Post analysis result entity
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Post analysis result entity
 */
@Parcelize
class PostAnalysisResultEntity(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("status")
    val status: Long,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("message")
    val message: PostAnalysisResultMessageEntity,
    @SerializedName("path")
    val path: String
) : Parcelable

/**
 * Post analysis result message entity
 *
 * @property acknowledge
 * @constructor Create empty Post analysis result message entity
 */
@Parcelize
data class PostAnalysisResultMessageEntity(
    @SerializedName("acknowledge")
    val acknowledge: String
) : Parcelable