package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Registered device entity
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Registered device entity
 */
@Parcelize
class RegisteredDeviceEntity(
    val timestamp: Long,
    val status: Long,
    val statusMessage: String,
    val message: MessageImeiEntity,
    val path: String
): Parcelable