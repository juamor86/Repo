package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicReleasesSectionEntity(
    val meta: Meta,
    val releases: List<DynamicReleasesEntity>,
    val resourceType: String
) : Parcelable {

    @Parcelize
    data class Meta(
        val lastUpdated: String
    ) : Parcelable
}
