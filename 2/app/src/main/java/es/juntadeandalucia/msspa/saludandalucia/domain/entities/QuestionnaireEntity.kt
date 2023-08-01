package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.data.entities.Meta
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Code
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Identifier
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionnaireEntity(
    val title: String,
    val questions: List<QuestionEntity>,
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
