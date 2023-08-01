package es.juntadeandalucia.msspa.authentication.data.factory.entities

data class LoginResponseData(
    val access_token: String,
    val token_type: String,
    val expires_in: String,
    val scope: String,
    val active: Boolean,
    val totp_secret_key: String,
    val refresh_token: String
)
