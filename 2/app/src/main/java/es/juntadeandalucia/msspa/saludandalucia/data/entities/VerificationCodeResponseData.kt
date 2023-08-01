package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class VerificationCodeResponseData(@Transient override val issue: List<MSSPAIssueData>) :
    MSSPAResponseData(issue) {

    val idVerification: String
        get() {
            return super.issue?.get(0)?.run { details.text } ?: ""
        }
}
