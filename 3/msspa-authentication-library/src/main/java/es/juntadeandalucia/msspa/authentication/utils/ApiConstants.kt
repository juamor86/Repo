package es.juntadeandalucia.msspa.authentication.utils

object ApiConstants {

    object General {
        const val URL_API_V1 = "api/v1.0"
        const val HTTP_CONNECT_TIMEOUT = 60 * 1000.toLong()
        const val HTTP_READ_TIMEOUT = 60 * 1000.toLong()
        const val CONTENT_TYPE_HEADER = "Content-Type"
        const val ACCEPT_HEADER = "Accept"
        const val APP_KEY_HEADER = "appKey"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val ID_DEVICE_HEADER = "idDevice"
        const val ID_DEVICE_MSSPA_HEADER = "idDeviceMsspa"
        const val API_KEY_HEADER = "apiKey"
        const val APPLICATION_JSON = "application/json"
        const val DEFAULT = "default"
        const val LOCATION = "Location"
        const val STATUS_ERROR = "status=error"
        const val INVALID_BDU_PHONE = "Invalid BDU phone"
        const val INVALID_BDU_DATA = "Invalid BDU data"
        const val INVALID_LOGIN_REPEATED = "The session has expired or already been granted. The login process has to be repeated to be successful"
        const val MAX_SMS_ATTEMPS = "Datos Conocidos SMS BDU"
        const val INVALID_PIN_SMS = "Invalid PIN SMS received"
        const val PROTECTED_USER = "protected_user"
        const val NOT_PRIVATE_HEALTH_CARE_USER = "only_private_healthcare_allowed"
        const val QR_LOGIN_ERROR = "Unable to authenticate with 'QR"
        const val INVALID_REQUEST = "invalid_request"
        const val WRONG_DATA_QR = "Wrong data in login with 'QR'"
        const val SEND_NOTIFICATION_ERROR = "send_notification_error"
        const val CODE_SMS_LENGTH = 6
        const val PHONE_NUMBER_LENGTH = 9
        const val KEY_DURATION: Int = 100
        const val EVENT_DEEPLINK = "://event?send="
    }

    object LoginApi {
        const val RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE_CODE = "code"
        const val CLIENT_ID = "client_id"
        const val SCOPE = "scope"
        const val SCOPE_CIUDADANO = "ciudadano"
        const val STATE = "state"
        const val REDIRECT_URI = "redirect_uri"
        const val ACTION = "action"
        const val ACTION_LOGIN = "login"
        const val LOGIN_METHOD = "login_method"
        const val LOGIN_METHOD_DATOS = "datos_conocidos"
        const val LOGIN_METHOD_DATOS_SMS = "datos_conocidos_sms_bdu"
        const val LOGIN_METHOD_DATOS_NO_NUHSA = "datos_conocidos_sintarjeta"
        const val LOGIN_METHOD_QR = "qr"
        const val LOGIN_METHOD_DNIE = "afirma"
        const val QR_ACCESS_TOKEN = "qr_access_token"
        const val SESSION_ID = "sessionID"
        const val SESSION_DATA = "sessionData"
        const val TOKEN_TYPE = "tokenType"
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
        const val TOTP_SECRET_KEY = "rtTotpSeed"
        const val EXPIRES_IN = "expiresIn"
        const val TOKEN = "token"
        const val ID_TYPE = "idtype"
        const val IDENTIFIER = "identifier"
        const val DNI = "dni"
        const val NIF_NIE = "nif_nie"
        const val NUSS = "nuss"
        const val NUHSA = "nuhsa"
        const val BIRTHDAY = "birthday"
        const val PHONE_NUMBER = "tlf_number"
        const val STEP = "step"
        const val STEP_1 = "1"
        const val STEP_2 = "2"
        const val PIN_SMS = "pin_sms"
        const val JWT = "jwt"
        const val JTI = "jti"
        const val KEY_LOGIN_QR = "key_login_qr"
    }

    object TokenApi {
        const val GRANT_TYPE = "grant_type"
        const val REFRESH_TOKEN_TOTP = "refresh_token_totp"
        const val REFRESH_TOKEN = "refresh_token"
        const val TOTP = "totp"
    }


    object Arguments {
        const val ARG_AUTHORIZE = "authorize_arg"
        const val ARG_USER = "user_arg"
        const val ARG_SAVE_USER = "saveuser_arg"
        const val ARG_QUIZ_AUTHORIZE = "quiz_authorize_arg"
        const val ARG_AUTHENTITY = "authentity_arg"
        const val ARG_QR_CODE = "qr_code_arg"
        const val ARG_QR_CLEAN_FORM = "qr_code_clean_form"
    }

    object Preferences {
        const val PREF_NAME = "preferences"
        const val PREF_USER_LOGGED = "pref_user_logged"
        const val PREF_FIRST_ACCESS = "first_access"
        const val PREF_FIRST_ACCESS_ON_BOARDING = "first_access_on_boarding"
        const val PREF_SAVED_USERS = "pref_saved_users"
        const val PREF_FIRST_SAVE_USER_ADVICE = "pref_first_save_user_advice"
        const val PREF_FIRST_LOAD_USER_ADVICE = "pref_first_load_user_advice"
        const val PREF_PIN = "pref_pin"
        const val PREF_FIREBASE_TOKEN = "pref_firebase_token"
        const val PREF_NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val PREF_NOTIFICATION_SUBSCRIPTION_PHONE_NUMBER = "notifications_phone_number"
        const val PREF_FIRST_ACCESS_TO_SCAN_CERTIFICATE = "first_access_scan"
        const val PREF_FIRST_ACCESS_TO_VALIDATE_CERTIFICATE = "first_access_validate"
        const val PREF_FIRST_LOGGING_QR_ATTEMPTS = "pref_first_loggin_qr_attempts"
    }

    object KeyNameCipher{
        const val KEY_SAVED_USERS = "key_name_cipher_users"
        const val KEY_SAVED_PIN = "key_name_cipher_pin"
    }

    object ErrorCode {
        const val DEFAULT_ERROR_CODE = 400
        const val TOO_MANY_REQUEST_ERROR_CODE = 429
    }
}