package es.juntadeandalucia.msspa.saludandalucia.data.entities

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ValueCodingData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerData(
    val valueBoolean: Boolean? = null,
    val valueDate: String? = null,
    val valueTime: String? = null,
    val valueDateTime: String? = null,
    val valueInteger: String? = null,
    val valueString: String? = null,
    val valueUri: String? = null,
    val valueCoding: ValueCodingData? = null,
    val valueQuantity: ValueQuantity? = null,
    val valueAttachment: ValueAttachment? = null,
    val valueCode: String? = null,
    val valueDecimal: String? = null,
    val extension: List<ExtensionData> = listOf()
) : Parcelable

@Parcelize
data class ValueQuantity(
    val value: String = "",
    val unit: String = ""
) : Parcelable

@Parcelize
data class ValueAttachment(
    val title: String = "",
    val data: String = "",
    val size: Long = 0L,
    val creation: String = ""
) : Parcelable


