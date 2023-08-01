package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicSectionEntity(
    val meta: Meta,
    val screens: List<DynamicScreenEntity>,
    val resourceType: String
) : Parcelable {

    @Parcelize
    data class Meta(
        val lastUpdated: String
    ) : Parcelable
}
