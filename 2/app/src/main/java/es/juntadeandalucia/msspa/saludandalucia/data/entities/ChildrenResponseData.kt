package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class ChildrenResponseData(
    val access_level: String,
    val id: String,
    val type: String?,
    val target: String?,
    val title: Title?
)