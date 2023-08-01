package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class LoginResponseData(
    val access_token: String,
    val token_type: String,
    val expires_in: String,
    val scope: String,
    val active: Boolean
)
