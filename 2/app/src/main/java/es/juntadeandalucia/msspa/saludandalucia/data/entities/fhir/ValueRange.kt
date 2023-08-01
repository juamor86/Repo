package es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValueRange(val high: Int = 1, val low: Int = 1) : Parcelable
