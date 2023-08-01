package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.data.entities.Meta
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.Code
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.Identifier
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData

import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynQuestionnaireEntity(
    val title: String,
    val questions: List<DynQuestionEntity>,
    val code: List<Code> = listOf(),
    val date: String = "",
    val extension: List<ExtensionData> = listOf(),
    val id: String = "",
    val identifier: List<Identifier> = listOf(),
    val item: List<ItemData> = listOf(),
    val meta: Meta = Meta(),
    val name: String = "",
    val purpose: String = "",
    val resourceType: String = "",
    val status: String = ""
):Parcelable
