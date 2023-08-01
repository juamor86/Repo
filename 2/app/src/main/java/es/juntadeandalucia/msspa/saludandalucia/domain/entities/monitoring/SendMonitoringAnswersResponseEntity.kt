package es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class SendMonitoringAnswersResponseEntity(
    val date: String,
    val message: String
) : Parcelable
