package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppointmentEntity(
    val id: String = "",
    val date: String = "",
    val center: String = "",
    val topic: String = "",
    val task: String = ""
) : Parcelable
