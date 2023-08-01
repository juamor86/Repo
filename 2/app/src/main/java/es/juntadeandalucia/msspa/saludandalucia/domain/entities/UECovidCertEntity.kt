package es.juntadeandalucia.msspa.saludandalucia.domain.entities

data class UECovidCertEntity(
    var dni: String? = null,
    var userName: String? = null,
    var userLastName: String? = null,
    var birthdate: String? = null,
    var fullName: String? = null,
    var isOk: Boolean = true
)
