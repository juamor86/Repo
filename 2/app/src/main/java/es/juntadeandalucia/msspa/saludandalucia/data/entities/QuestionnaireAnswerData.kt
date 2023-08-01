package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Author
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Subject

class QuestionnaireAnswerData(
    @SerializedName("author")
    val author: Author? = null,
    @SerializedName("authored")
    val authored: String = "",
    @SerializedName("item")
    val item: List<ItemData> = listOf(),
    @SerializedName("questionnaire")
    val questionnaire: String = "",
    @SerializedName("resourceType")
    val resourceType: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("subject")
    val subject: Subject? = null
)
