package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Other apk entity
 *
 * @property data
 * @property services
 * @constructor Create empty Other apk entity
 */
@Parcelize
class OtherApkEntity(
    val data: String, val services: List<ServiceEntity>
) : Parcelable