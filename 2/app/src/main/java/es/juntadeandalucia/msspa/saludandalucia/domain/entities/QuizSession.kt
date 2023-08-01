package es.juntadeandalucia.msspa.saludandalucia.domain.entities

data class QuizSession(val tokenType: String, val accessToken: String) {

    val authorizationToken: String?
        get() {
            if (tokenType.isNotBlank() && accessToken.isNotBlank()) {
                return "$tokenType $accessToken"
            }
            return null
        }

}
