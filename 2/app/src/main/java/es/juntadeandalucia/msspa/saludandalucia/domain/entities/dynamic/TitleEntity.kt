package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TitleEntity(
    val alt: String = "",
    val color: String = "#ffffff",
    val text: String = ""
) : Parcelable
