package es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Meta
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Author

data class SendDynQuestAnswersResponseData(
    val author: Author = Author(),
    val authored: String = "",
    val extension: List<ExtensionData> = listOf(),
    val id: String = "",
    val item: List<ItemData> = listOf(),
    val meta: Meta = Meta(),
    val questionnaire: String = "",
    val resourceType: String = "",
    val status: String = ""
)
