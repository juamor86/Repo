package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class ParameterResponseData(
    val children: List<ChildrenResponseData> = listOf(),
    val id: String
)