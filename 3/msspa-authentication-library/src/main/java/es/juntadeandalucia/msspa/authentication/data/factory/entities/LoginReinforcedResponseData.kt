package es.juntadeandalucia.msspa.authentication.data.factory.entities

data class LoginReinforcedResponseData(
        val result: String,
        val description: String,
        val sessionID: String,
        val sessionData: String
)