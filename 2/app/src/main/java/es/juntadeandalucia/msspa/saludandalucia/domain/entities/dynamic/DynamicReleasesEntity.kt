package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicReleasesEntity(
    val id: String,
    val background: String?,
    val header: String?,
    val title: String,
    val titleAlt: String?,
    val children: List<DynamicReleasesElementEntity>?,
    val displayCheck: DisplayCheckEntity?

) : Parcelable {
    @Parcelize
    data class DisplayCheckEntity(
        val id: String,
        val title: String
    ) : Parcelable
}


