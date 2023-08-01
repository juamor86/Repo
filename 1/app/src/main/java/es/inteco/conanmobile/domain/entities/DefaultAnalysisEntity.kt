package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Default analysis entity
 *
 * @property title
 * @property subtitle
 * @constructor Create empty Default analysis entity
 */
@Parcelize
data class DefaultAnalysisEntity(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String
) : Parcelable