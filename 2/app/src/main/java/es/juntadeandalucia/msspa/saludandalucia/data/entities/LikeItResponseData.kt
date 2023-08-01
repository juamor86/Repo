package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class LikeItResponseData(
    val meta: Meta,
    val parameter: List<ParameterResponseData>,
    val resourceType: String
)