package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationEntity(
    var id: String,
    var title: String,
    var description: String,
    var date: Long,
    var readed: Boolean
) : Parcelable
