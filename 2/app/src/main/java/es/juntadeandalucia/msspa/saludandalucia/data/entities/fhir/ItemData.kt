package es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnswerData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemData(
    val answer: List<AnswerData>? = listOf(),
    val answerOption: List<AnswerData>? = listOf(),
    val extension: List<ExtensionData>? = listOf(),
    val item: MutableList<ItemData> = mutableListOf(),
    val linkId: String = "",
    val text: String = "",
    val required: Boolean = false,
    val type: String? = null,
    val enableWhen: List<EnableWhenData>? = listOf()
): Parcelable{

    override fun equals(other: Any?): Boolean {
        return other is ItemData && other.linkId == linkId
    }
}
