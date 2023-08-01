package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubtitleEntity(
    val color: String = "#ffffff",
    val text: String = ""
) : Parcelable
