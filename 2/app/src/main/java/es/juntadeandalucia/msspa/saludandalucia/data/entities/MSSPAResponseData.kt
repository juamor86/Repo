package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

open class MSSPAResponseData(
    open val issue: List<MSSPAIssueData>?,
    val error: String? = null,
    @SerializedName("error_description") val errorDescription: String? = null
) {

    companion object {
        const val MAX_ATTEMPTS_EXCEEDED = "004"
        const val FORBIDDEN = "403"
        const val MAX_ATTEMPTS = "max_attemps"
        const val LOGIN_REQUIRED = "login_required"
        const val UNDER_16 = "No autorizado (menor de 16)"
        const val NO_MONITORING_PROGRAM = "El paciente no tiene un programa de atención personalizada asociado"
    }

    val issueErrorCode: String?
        get() {
            return issue?.get(0)?.code
        }

    val issueDetailsText: String?
        get() {
            return issue?.get(0)?.details?.text
        }

    val issueDiagnostic: String?
        get() {
            return issue?.get(0)?.diagnostics
        }
}
