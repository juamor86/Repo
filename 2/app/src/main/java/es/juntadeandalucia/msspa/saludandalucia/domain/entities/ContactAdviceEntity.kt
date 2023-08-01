package es.juntadeandalucia.msspa.saludandalucia.domain.entities

data class ContactAdviceEntity(
    val name: String,
    val number: String,
    val type: Int,
    val label: String
)
