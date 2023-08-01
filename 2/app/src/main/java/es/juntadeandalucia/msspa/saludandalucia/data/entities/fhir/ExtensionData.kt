package es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExtensionData(
    val url: String = "",
    val valueBoolean: Boolean = false,
    val valueDateTime: String = "",
    val valueInteger: Int = 0,
    val valueString: String = "",
    val valueCoding: ValueCodingData = ValueCodingData(),
    val valueCode: String = "",
    val valueDecimal: Double = 0.0,
    val valueRange: ValueRange? = null
): Parcelable

