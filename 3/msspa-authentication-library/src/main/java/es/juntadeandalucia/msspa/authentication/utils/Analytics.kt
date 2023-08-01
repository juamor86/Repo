package es.juntadeandalucia.msspa.authentication.utils

object Analytics {
    internal const val PERSONAL_DATA_BASIC = "basic_login"
    internal const val PERSONAL_DATA_SMS = "sms_login"
    internal const val PERSONAL_DATA_NO_CARD = "sin_tarjeta_login"
    internal const val PERSONAL_DATA_NO_CARD_SMS = "sin_tarjeta_sms"
    internal const val LOGIN_QR = "qr_login"

    private const val SUCCESS="_ok"
    private const val FAILURE="_ko"
    private const val AUTH_HEADER = "auth_"

    fun buildEvent(typeLogin: String, success: Boolean): String {
        return AUTH_HEADER + typeLogin + if (success) SUCCESS else FAILURE
    }
}