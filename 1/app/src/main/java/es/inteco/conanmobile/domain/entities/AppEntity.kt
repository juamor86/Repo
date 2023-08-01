package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * App entity
 *
 * @property appId
 * @property name
 * @property description
 * @property alt
 * @property category
 * @property icon
 * @property link
 * @property packageName
 * @property images
 * @constructor Create empty App entity
 */
@Parcelize
data class AppEntity(
    var appId: String,
    var name: String,
    var description: String,
    var alt: String,
    var category: String,
    var icon: String,
    var link: String,
    var packageName: String,
    var images: List<String>
) : Parcelable