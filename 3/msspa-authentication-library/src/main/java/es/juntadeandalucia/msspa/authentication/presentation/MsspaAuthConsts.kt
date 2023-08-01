package es.juntadeandalucia.msspa.authentication.presentation

import android.net.Uri
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity

class MsspaAuthConsts {

    enum class LoginMethod(val mode: String) {
        QR("1"),
        PERSONAL_DATA("7"),
        PERSONAL_DATA_BDU("8"),
        PERSONAL_DATA_NO_NUHSA("9"),
        DNIE("10");

        companion object {
            fun getMethod(mode: String?): LoginMethod? {
                for (method in LoginMethod.values()) {
                    if (method.mode == mode) {
                        return method
                    }
                }
                return null
            }
        }
    }

    companion object {

        fun getUrl(config: MsspaAuthenticationConfig, scope: MsspaAuthenticationManager.Scope): String {
            return Uri.parse(config.environment.url).buildUpon()
                    .appendEncodedPath(MSSPA_API)
                    .appendQueryParameter(RESPONSE_TYPE_PARAM, RESPONSE_TYPE_VALUE)
                    .appendQueryParameter(CLIENT_ID_PARAM, config.apiKey)
                    .appendQueryParameter(APP_VERSION_PARAM, config.version)
                    .appendQueryParameter(ID_SO_PARAM, config.idSo)
                    .appendQueryParameter(LEVEL_AUTENTICATION, scope.level)
                    .toString()
        }

        internal const val THIRTY_YEARS = 30*365*24*60*60*100L
        private const val RESPONSE_TYPE_PARAM = "response_type"
        private const val RESPONSE_TYPE_VALUE = "code"
        private const val CLIENT_ID_PARAM = "client_id"
        private const val SCOPE_PARAM = "scope"
        private const val REDIRECT_URI_PARAM = "redirect_uri"
        private const val APP_VERSION_PARAM = "app_version"
        private const val ID_SO_PARAM = "id_so"
        private const val ID_SO_PARAM_VALUE = "0"
        private const val LEVEL_AUTENTICATION = "level"
        const val AUTHENTICATION_HOST = "authentication"

        const val AUTHENTICATION_LOGIN_METHOD = "login_method"
        const val AUTHENTICATION_SESSION_ID = "sessionID"
        const val AUTHENTICATION_SESSION_DATA = "sessionData"
        const val LOGIN_METHOD_ARG = "method"
        const val AUTH_CONFIG_ARG = "config"
        const val AUTH_ARG = "auth_entity"
        const val LEVEL_ARG = "level"
        const val QUIZ_USER_ARG = "quiz_user"
        const val FRAGMENT_DEST_ARG = "fragment_dest"

        const val TOTP_ACTION = "es.juntadeandalucia.msspa.authentication.totp"
        const val TOTP_CODE = "totp"

        internal const val CAMERA_PERMISSION_REQUEST_CODE = 100

        private const val MSSPA_API = "api/v1.0/oauth2/authorize"
        private const val SCOPE_VALUE = "ciudadano"

        internal const val NUHSA_PREFIX = "AN"
        internal const val BIRTHDAY_FORMAT = "dd/MM/yyyy"
        internal const val BIRTHDAY_MIN_YEARS = 30
        internal const val MAX_NUHSA_LENGTH = 12
        internal const val MIN_NUSHA_LENGHT = 10
        internal const val MAX_NUSS_LENGHT = 13
        internal const val MIN_NUSS_LENGHT = 12
        internal const val MAX_IDENTIFIER_LENGTH = 9
        internal const val REFRESH_OTP_TIME_MILLIS: Long = 45000
        internal const val SPAIN_PREFIX = "+34"
        internal const val PLUS = "+"
        internal const val VALIDATION_MAX_LENGTH_SPANISH_PHONE_NUMBER = 9
        internal const val PIN_LENGHT = 4
        internal const val MAX_PIN_ATTEMPTS = 3
        internal const val DEFAULT_DNI_UNDER_AGE = "00000000T"

        internal val documentTypes: List<KeyValueEntity> = listOf(
                KeyValueEntity("1", "DNI"),
                KeyValueEntity("6", "NIE"),
                KeyValueEntity("2", "Pasaporte"),
                KeyValueEntity("3", "Tarjeta Residencia comunitaria"),
                KeyValueEntity("5", "Otros"))

        const val RESULT_ERROR_EXTRA = "resultError"
        const val RESULT_EXTRA = "result"

        internal const val API_BASE_HOST_PRE = "https://ws237.sspa.juntadeandalucia.es:9443"
        internal const val API_BASE_HOST_PRO = "https://ws238.sspa.juntadeandalucia.es:9443"
        internal const val URL_WEB_AUTENTIFICATION_QR_PRE = "https://www.sspa.juntadeandalucia.es/servicioandaluzdesalud/autenticacionmsalud_pre/webautenticacion/oauth.xhtml"
        internal const val URL_WEB_AUTENTIFICATION_QR_PRO = "https://sspa.lajunta.es/qrmovil"
        internal const val HELP_LOGIN_URL = "https://www.sspa.juntadeandalucia.es/servicioandaluzdesalud/msspa_gateway/autenticacion/ayuda"

    }

    object QueryParams {
        internal const val STATUS = "status"
        internal const val STATUS_VALUE_OK = "ok"
        internal const val STATUS_VALUE_ERROR = "error"
        internal const val TOKEN_TYPE = "tokenType"
        internal const val ACCESS_TOKEN = "accessToken"
        internal const val EXPIRES_IN = "expiresIn"
        internal const val SCOPE = "scope"
        internal const val EXTERNAL_LINK = "external_link"
    }

    object Validations {
        const val VALIDATION_MIN_LENGTH_NUSHA = 12
        const val VALIDATION_MIN_LENGTH_BIRTHDAY = 10
        const val VALIDATION_MIN_LENGTH_IDENTIFIER = 9
        const val VALIDATION_MIN_LENGTH_SPANISH_PHONE = 9
    }
}
