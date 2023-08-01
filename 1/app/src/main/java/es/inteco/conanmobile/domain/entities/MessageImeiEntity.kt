package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Message imei entity
 *
 * @property idTerminal
 * @property key
 * @constructor Create empty Message imei entity
 */
@Parcelize
class MessageImeiEntity(
    val idTerminal: String,
    val key: String
): Parcelable