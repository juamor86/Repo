package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Author
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Subject

data class SendNewMonitoringAnswerData(
    @SerializedName("author")
    val author: Author? = null,
    @SerializedName("authored")
    val authored: String = "",
    @SerializedName("extension")
    var extension: List<ExtensionData> = listOf(),
    @SerializedName("item")
    val item: List<ItemData> = listOf(),
    @SerializedName("questionnaire")
    val questionnaire: String = "",
    @SerializedName("resourceType")
    val resourceType: String = "QuestionnaireResponse",
    @SerializedName("status")
    val status: String = "completed",
    @SerializedName("subject")
    val subject: Subject? = null
)
