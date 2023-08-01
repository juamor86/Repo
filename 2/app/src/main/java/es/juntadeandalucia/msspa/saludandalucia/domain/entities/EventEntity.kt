package es.juntadeandalucia.msspa.saludandalucia.domain.entities

data class EventEntity(
    var counter: Int = 0,
    val type: String? = null,
    val accessLevel: String
)
