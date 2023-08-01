package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeaturedEntity(
    var title: String?,
    var subtitle: String?,
    var operationMode: String?,
    var linkMode: String,
    var imgHeader: String?
) : Parcelable
