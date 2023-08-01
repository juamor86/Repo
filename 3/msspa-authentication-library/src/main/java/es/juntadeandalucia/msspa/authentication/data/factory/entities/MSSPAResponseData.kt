package es.juntadeandalucia.msspa.authentication.data.factory.entities

import com.google.gson.annotations.SerializedName

open class MSSPAResponseData (
    open val issue: List<MSSPAIssueData>?,
    val error: String? = null,
    @SerializedName("error_description") val errorDescription: String? = null
) {
    companion object {
        const val MAX_ATTEMPTS_EXCEEDED = "004"
        const val MAX_ATTEMPTS = "max_attemps"
        const val LOGIN_REQUIRED = "login_required"
    }

    val issueErrorCode: String?
        get() {
            return issue?.get(0)?.code
        }
}