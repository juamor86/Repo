package es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring

import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringData.Meta

data class MonitoringListData(
    val entry: MutableList<Entry> = mutableListOf(),
    val id: String = "",
    val link: List<Link> = listOf(),
    val meta: Meta = Meta(""),
    val resourceType: String = "",
    val total: Int = 0,
    val type: List<String> = listOf()
)

data class Entry(
    val resource: MutableList<Resource> = mutableListOf()
)

data class Link(
    val relation: String = "",
    val url: String = ""
)

data class Resource(
    val author: Author = Author(),
    val authored: String = "",
    val extension: List<ExtensionData> = listOf(),
    val id: String = "",
    val item: List<ItemData> = listOf(),
    val meta: Meta = Meta(""),
    val questionnaire: String = "",
    val resourceType: String = "",
    val status: String = "",
    val subject: Subject = Subject()
)

data class Author(
    val display: String = "",
    val extension: List<ExtensionData> = listOf(),
    val type: String = ""
)

data class Subject(
    val extension: List<ExtensionData> = listOf(),
    val id: String = "",
    val reference: String = "",
    val type: String = ""
)
