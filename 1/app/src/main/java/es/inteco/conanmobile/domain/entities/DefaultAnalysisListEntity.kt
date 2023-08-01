package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Default analysis list entity
 *
 * @property listDefaultAnalysisEntity
 * @constructor Create empty Default analysis list entity
 */
@Parcelize
data class DefaultAnalysisListEntity(
    @SerializedName("listDefaultAnalysisEntity")
    val listDefaultAnalysisEntity: List<DefaultAnalysisEntity>
) : Parcelable