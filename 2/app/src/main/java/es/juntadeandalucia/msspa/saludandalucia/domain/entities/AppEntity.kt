package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
