package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * O s i entity
 *
 * @property id
 * @property title
 * @property description
 * @constructor Create empty O s i entity
 */
@Parcelize
class OSIEntity(
    val id: String,
    val title: Map<String, String>,
    val description: Map<String, String>,
): Parcelable