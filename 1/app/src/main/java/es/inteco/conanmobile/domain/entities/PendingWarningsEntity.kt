package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Pending warnings entity
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Pending warnings entity
 */
@Parcelize
class PendingWarningsEntity(
    val timestamp: String,
    val status: Long,
    val statusMessage: String,
    val message: PendingWarningsMessageEntity,
    val path: String
) : Parcelable

/**
 * Pending warnings message entity
 *
 * @property haveNotifications
 * @constructor Create empty Pending warnings message entity
 */
@Parcelize
data class PendingWarningsMessageEntity(
    val haveNotifications: Boolean
) : Parcelable