package es.juntadeandalucia.msspa.saludandalucia.data.utils

import es.juntadeandalucia.msspa.saludandalucia.BuildConfig

object ApiConstants {

    object General {
        const val API_BASE_URL = BuildConfig.API_BASE_HOST
        const val URL_API_V1 = "api/v1.0"
        const val URL_API_V1_1 = "api/v1.1"
        const val HTTP_CONNECT_TIMEOUT = 60 * 1000.toLong()
        const val HTTP_READ_TIMEOUT = 60 * 1000.toLong()
        const val CONTENT_TYPE_HEADER = "Content-Type"
        const val ACCEPT_HEADER = "Accept"
        const val APP_KEY_HEADER = "appKey"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val ID_DEVICE_HEADER = "idDevice"
        const val ID_DEVICE_MSSPA_HEADER = "idDeviceMsspa"
        const val BEARER_HEADER = "Bearer "
        const val API_KEY_HEADER = "apiKey"
        const val COMPLETE_PARAM = "completa"
        const val APPLICATION_JSON = "application/json"
        const val DATE_START = "start"
        const val DATE_END = "end"
        const val DEFAULT = "default"
        const val FALSE = "false"
        const val SALUD_ANDALUCIA_APP_KEY_IDENTIFICATION = BuildConfig.APP_KEY_IDENTIFICATION
        const val SALUD_ANDALUCIA_API_KEY_IDENTIFICATION = BuildConfig.API_KEY_IDENTIFICATION
        const val SALUD_ANDALUCIA_LEGACY_APP_IDENTIFICATION = BuildConfig.LEGACY_APP_IDENTIFICATION
        const val SCOPE = BuildConfig.SCOPE
        const val APP_VERSION_PARAM = "app_version"
        const val ID_SO_PARAM = "id_so"
        const val ID_SO_PARAM_VALUE = "0"
        const val DYNAMIC_SCREEN_TYPE = "type"
        const val SCOPE_PARAM = "scope"
        const val HEADER_LOCATION = "location"
        const val QUERY_PARAM_ERROR_DESCRIPTION = "error_description"
        const val STATUS_ERROR = "status=error"
        const val ACTIONS = "actions"
    }


    object Common {
        const val HTTP_CONNECT_TIMEOUT = 60 * 1000.toLong()
        const val HTTP_READ_TIMEOUT = 60 * 1000.toLong()
        const val CONTENT_TYPE_HEADER = "Content-Type"
        const val ACCEPT_HEADER = "Accept"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val APPLICATION_JSON = "application/json"
        const val TIPE_INFORM = "tipoInforme"
        const val COUNT = "_count"
        const val PAGE = "page"
        const val SCROLL = "scroll"
        const val CHANGE = "change"
        const val URL_API_V2 = "api/v2.0"
        const val URL_API_V1 = "api/v1.0"
        const val DATE_START = "start"
        const val DATE_END = "end"
        const val TOKEN = "token"
        const val BEARER_TOKEN = "Bearer"
    }

    object Params {
        const val QUERY_PARAM_PAGE = "page"
        const val QUERY_PARAM_COUNT = "_count"
        const val QUERY_VALUE_COUNT = "20"
        const val QUERY_PARAM_COMPLETE = "completa"
        const val DEFAULT_QUERY_PARAM_COMPLETE = "true"
        const val COMPLETE_PARAM = "completa"
        const val DEFAULT = "default"
        const val FALSE = "false"
        const val DEFAULT_QUERY_PARAM_COUNT = 10
        const val PAGE_PARAM = "page"
        const val PAGE_PARAM_VALUE = "1"
        const val COUNT_PARAM = "_count"
        const val COUNT_PARAM_VALUE = 50
        const val PARAM_COUNT_DOUBLE_VALUE = "100"
        const val DYNAMIC_SCREEN_PARAM_TYPE = "screens"
        const val TOKEN = "token"
    }

    object NewsApi {
        const val TYPE_FEATURED = "destacado"
        const val TYPE = "type"
        const val TYPE_BANNER = "banner"
        const val TYPE_CONTENT = "contenido"
    }

    object QuizLoginApi {
        const val RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE_CODE = "code"
        const val CLIENT_ID = "client_id"
        const val STATE = "state"
        const val STATE_SALUD_ANDALUCIA = "saludandalucia"
        const val REDIRECT_URI = "redirect_uri"
        const val REDIRECT_URI_VALUE = "msspa.app.102://autentication"
        const val ACTION = "action"
        const val ACTION_LOGIN = "login"
        const val LOGIN_METHOD = "login_method"
        const val LOGIN_METHOD_DATOS_SMS = "datos_conocidos_sms"
        const val SESSION_ID = "sessionID"
        const val SESSION_DATA = "sessionData"
        const val ID_TYPE = "idType"
        const val NIF_NIE = "identifier"
        const val NUHSA = "nuhsa"
        const val BIRTHDAY = "birthday"
        const val PHONE_NUMBER = "tlf_number"
        const val STEP = "step"
        const val STEP_1 = "1"
        const val STEP_2 = "2"
        const val PIN_SMS = "pin_sms"
    }

    object QuizApi {
        const val PHONE_NUMBER = "tlf_number"
        const val TIP_PROFESSIONAL = "tip_profesional"
    }

    object AppsApi {
        const val TYPE = "type"
        const val TYPE_ANDROID = "android"
        const val TYPE_HUAWEI = "huawei"
    }

    object ErrorCode {
        const val DEFAULT_ERROR_CODE = 400
        const val UNAUTHORIZED = 401
        const val TOO_MANY_REQUEST_ERROR_CODE = 429
        const val FORBIDDEN_ERROR_CODE = 403
        const val NO_CERTIFICATE_ERROR_CODE = 404
    }

    object ErrorContentDescription {
        const val INVALID_BDU_DATA = "Invalid BDU data"
        const val MAX_ATTEMPTS = "Unable to create a SMS session with 'Datos Conocidos SMS'. SESSION_ERROR: Max session count reached"
        const val PROTECTED_USER = "protected_user"
    }

    object NotificationsSubscriptionApi {
        const val VERIFICATION_CODE = "codigoVerificacion"
        const val VERIFICATION_ID = "idVerificacion"
        const val TOKEN = "tokenRegistro"
        const val CONTACT = "contact"
        const val CHANNEL = "channel"
        const val ID_DEVICE = "idDevice"
        const val NOTIFICATION_ID = "gcm.message_id"
        const val NOTIFICATION_TITLE = "title"
        const val NOTIFICATION_BODY = "body"
        const val NOTIFICATION_TYPE = "type"
        const val NOTIFICATION_CONFIRM_PAYLOAD = "confirm_request"
    }

    object CovidCert {
        const val DISEASE_PARAM = "enfermedad"
        const val COVID_VALUE = "covid19"
        const val FORMAT_HEADER = "format"
        const val FORMAT_HEADER_INIT = "View"
        const val FORMAT_HEADER_PRINT = "Print"
        const val ACTION = "action"
        const val TYPE_PARAM = "type"
    }

    object Dynamic {
        const val DYNAMIC_PARAM_TYPE = "type"
        const val DYNAMIC_LAYOUT_TYPE = "layouts"
        const val DYNAMIC_AREAS_TYPE = "areas"
        const val DYNAMIC_RELEASES_TYPE = "new_releases"
    }

    object Monitoring {
        const val PROGRAM_ID_PARAM = "id"
        const val USER_ID_PARAM = "user_id"
        const val AUTHORED_PARAM = "authored"
        const val VERSION_PARAM = "version"
        const val QUESTIONNAIRE_PARAM = "questionnaire"
        const val VERSION_MOCK = "mock"
        const val VERSION_1_0 = "v1.0"
        const val VERSION_1_1 = "v1.1"
        const val COUNT = "500"
    }

    object QuestionnaireApi {
        const val HEADER_VERSION_MOCK = "version: sample-mock"
    }

    object Measurement {
        const val HELPER_TYPE = "AyudaMedicion"
        const val QUESTIONNAIRE_PARAM = "questionnaire"
        const val MEASURE_TYPE_PARAM = "tipo"
        const val MEASURE_TYPE_VALUE = "Mediciones"
        const val MEASURE_VERSION_PARAM = "version"
        const val MEASURE_VERSION_VALUE = "v1.0"
    }

    object NotificationReception {
        const val NOTIFICATION_COMMUNICATION_RESOURCETYPE = "Communication"
        const val NOTIFICATION_COMMUNICATON_STATUS = "completed"
    }
    
    object Advices {
        const val ADVICES_CRITERIA = "criteria"
        const val ADVICES_CONTACT = "contact"
        const val ADVICES_VERSION = "v2.0"
        const val VERSION_PARAM = "version"
        const val ADVICES_URL_PATCH_ID = "id"
        const val NUHSA = "[nuhsa]"
        const val EVENT = "evento"
        const val TYPE_URL_SUB_SHARED = "suscripcionCompartida"
        const val TYPE_URL_SUB_FATHER = "suscripcionPadre"
        const val SUBSCRIPTION = "Subscription"
        const val POST = "POST"
        const val PUT = "PUT"
        const val BUNDLE = "Bundle"
    }
}
