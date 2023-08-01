package es.juntadeandalucia.msspa.saludandalucia.utils

import android.text.InputFilter
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassTypeEntity




class Consts {
    object Analytics {

        /** Covid certificate events **/
        internal const val CERTIFICATE_CERT_ACCESS = "ccd_show_"
        internal const val CERTIFICATE_DOWNLOAD_OK = "ccd_[type]_download_ok"
        internal const val CERTIFICATE_DOWNLOAD_KO = "ccd_[type]_download_ko"
        internal const val CERTIFICATION_VALIDATION_OK = "ccd_verification_ok"
        internal const val CERTIFICATION_VALIDATION_KO = "ccd_verification_ko"
        internal const val SAVE_CERT_WALLET = "ccd_save_wallet_"
        internal const val WALLET_SCREEN_ACCESS = "scn_wallet"
        internal const val WALLET_DETAIL_SCREEN_ACCESS = "ccd_show_wallet_"
        internal const val CERTIFICATE_LIST = "certificates_list"
        internal const val LOGIN_COVID_TRIAGE_SCREEN_ACCESS = "login_covid_triage"
        internal const val LOGIN_COVID_TRIAGE_SECOND_FACTOR_SCREEN_ACCESS =
            "login_covid_triage_second_factor"
        internal const val QUIZ_COVID_TRIAGE_SCREEN_ACCESS = "quiz_covid_triage"
        internal const val QUIZ_COVID_RESULT_SCREEN_ACCESS =
            "quiz_covid_triage_result"
        internal const val COVID_CERTIFICATE_ADL_SCREEN_ACCESS =
            "covid_certificate"
        internal const val COVID_CERTIFICATE_QR_VALIDATION = "validate_covid_qr"

        /** Home events **/
        internal const val HOME_SCREEN_ACCESS = "home"
        internal const val DRAWER_SCREEN_ACCESS = "menu"
        internal const val USER_MENU_SCREEN_ACCESS = "user_menu"
        internal const val QUICK_LAUNCHER_SCREEN_ACCESS = "quick_launcher"
        internal const val APP_SCREEN_ACCESS = "apps"
        internal const val APP_DETAIL_SCREEN_ACCESS = "app_detail"
        internal const val PREFERENCES_SCREEN_ACCESS = "preferences"
        internal const val INTERNAL_WEBVIEW_SCREEN_ACCESS = "scn_int_wv_"
        internal const val DYNAMIC_SCREEN_SCREEN_ACCESS = "scn_dyn_"
        internal const val NEWS_SCREEN_ACCESS = "news"
        internal const val NEWS_DETAIL_SCREEN_ACCESS = "news_detail"
        internal const val LEGAL_SCREEN_ACCESS = "legal"
        internal const val ABOUT_SCREEN_ACCESS = "about"

        /** Followup and measurement events **/
        internal const val FOLLOWUP_INTERMEDIUM_ACCESS = "followup_intermedium"
        internal const val FOLLOWUP_MEASUREMENTS_ACCESS = "followup_measurements"
        internal const val FOLLOWUP_LIST_ACCESS = "followup_list"
        internal const val FOLLOWUP_DETAIL_ACCESS = "followup_detail"
        internal const val FOLLOWUP_DETAIL_EXTENDED_ACCESS = "followup_extended_detail"
        internal const val FOLLOWUP_CREATE_ACCESS = "followup_create"
        internal const val MEASUREMENTS_SCREEN_ACCESS = "measurements_list"
        internal const val MEASUREMENTS_SPECIFIC_ACCESS = "measurements_t_"
        internal const val MEASUREMENTS_FILTER_ACCESS = "measurements_f_"
        internal const val MEASUREMENTS_DETAIL_LIST_ACCESS = "measurements_dl_"
        internal const val MEASUREMENTS_DETAIL_GRAPHIC_ACCESS = "measurements_dg_"
        internal const val MEASUREMENTS_HELP_ACCESS = "measurements_h_"
        internal const val FOLLOWUP_CREATED_SUCESS = "followup_sended_ok"
        internal const val FOLLOWUP_CREATED_FAILURE = "followup_sended_ko"

        /** AviSAS and notifications **/
        internal const val AVISAS_HOME_ACCESS = "avisas_home"
        internal const val AVISAS_TYPE_HOME_ACCESS = "avisas_type_list"
        internal const val AVISAS_CREATE_ADVICE_SUCCESS = "avisas_create_ok"
        internal const val AVISAS_CREATE_ADVICE_FAILURE = "avisas_create_ko"
        internal const val AVISAS_DETAIL_ACCESS_FATHER = "avisas_dtl_c"
        internal const val AVISAS_DETAIL_ACCESS_CHILDREN = "avisas_dtl_r"
        internal const val AVISAS_MODIFIED_FATHER_SUCCESS = "avisas_dtl_c_mod_ok"
        internal const val AVISAS_MODIFIED_FATHER_FAILURE = "avisas_dtl_c_mod_ko"
        internal const val AVISAS_DELETED_SUCESS = "avisas_dtl_c_del_ok"
        internal const val AVISAS_DELETED_FAILURE = "avisas_dtl_c_del_ko"
        internal const val AVISAS_ADVICE_ACCEPTED_SUCCESS = "avisas_dtl_r_accept_ok"
        internal const val AVISAS_ADVICE_ACCEPTED_FAILURE = "avisas_dtl_r_accept_ko"
        internal const val AVISAS_REJECT_SUCESS = "avisas_dtl_r_reject_ok"
        internal const val AVISAS_REJECT_FAILURE = "avisas_dtl_r_reject_ko"
        internal const val AVISAS_MODIFIED_CHILDREN_SUCCESS = "avisas_dtl_r_mod_ok"
        internal const val AVISAS_MODIFIED_CHILDREN_FAILURE = "avisas_dtl_r_mod_ko"
        internal const val NOTIFICATION_STEP1_SCREEN_ACCESS = "notifications_step1"
        internal const val NOTIFICATION_STEP2_SCREEN_ACCESS = "notifications_step2"
        internal const val NOTIFICATIONS_SCREEN_ACCESS = "notifications"
        internal const val NOTIFICATION_DETAIL_SCREEN_ACCESS =
            "notification_detail"

        /** Additions events **/
        internal const val ADDITIONS_SHOW = "new_releases"

        /** Webview download **/
        internal const val DOWNLOAD_FILE_SUCCESS = "download_file_cs_ok"
        internal const val DOWNLOAD_FILE_FAILURE = "download_file_cs_ko"

        /** Dynamic questionnaires events **/
        internal const val DYN_QUEST_LANDING =  "dyn_quiz_landing"
        internal const val DYN_QUEST_QUIZ = "dyn_quiz"
        internal const val DYN_QUEST_QUIZ_DETAIL = "dyn_quiz_detail"
        internal const val DYN_QUEST_QUIZ_EXTENDED_DETAIL = "dyn_quiz_ext_detail"
        internal const val DYN_QUEST_CREATE = "dyn_quiz_create"
        internal const val DYN_QUEST_SEND_SUCCESS = "dyn_quiz_sended_ok"
        internal const val DYN_QUEST_SEND_FAILURE = "dyn_quiz_sended_ko"

    }



    companion object {

        internal const val ANDROID_ID_SO = "0"
        internal const val HUAWEI_ID_SO = "2"
        internal const val TRUSTLIST_CACHE = 24 * 60 * 60 * 1000L // One day millis
        internal const val OPTION = "option"
        internal const val LOGIN_SMS_STR = "covid-19"
        internal const val CODE_SMS_LENGTH = 6
        internal const val ON_BOARDING_DIALOG_TAG = "onBoardingDialog"
        internal const val ON_RELEASES_DIALOG_TAG = "onReleasesDialog"
        internal const val BUILD_TYPE_PRE = "Pre"
        internal const val BUILD_TYPE_PRO = "Pro"
        internal const val HUAWEI = "huawei"
        internal const val LINK_SALUD_ANDALUCIA_PLAY_STORE =
            "https://play.google.com/store/apps/details?id=es.juntadeandalucia.msspa.saludandalucia"
        internal const val LINK_SALUD_ANDALUCIA_APP_GALLERY =
            "https://appgallery.huawei.com/app/C101999551"
        internal const val PLUS_SCROLL_POSITION = 100
        internal const val LAST = "last"
        internal const val PAGE = "page"
        internal const val DATE_REX_LENGHT = 18
        internal const val SCROLL_DIRECTION_BOTTOM = 1
        internal const val MEASURE_MIN_LOAD_ITEMS = 10

        internal const val EVENT_PARAMETER_SEND = "send"

        internal const val TWO_MB_SIZE = 2000000L
        internal const val HTTP_WEB_START = "http://"
        internal const val HTTPS_WEB_START = "https://"

        // region File utils
        internal const val JPG_EXTENSION = ".jpg"
        internal const val PDF_EXTENSION = ".pdf"

        internal const val PDF_TYPE = "pdf"

        internal const val PDF_MIME_TYPE = "application/pdf"
        internal const val PNG_MIME_TYPE = "image/png"
        internal const val JPEG_MIME_TYPE = "image/jpeg"

        internal const val MB_FILE_SIZE = "MB"
        // region - Arguments
        internal const val AUTHORIZATION = "Authorization"
        internal const val ARG_AUTHORIZE = "authorize_arg"
        internal const val ARG_QUIZ = "quiz_arg"
        internal const val ARG_USER = "user_arg"
        internal const val ARG_PHONE_NUMBER = "phone_number_arg"
        internal const val ARG_NUHSA = "nuhsa_arg"
        internal const val ARG_BIRTHDAY = "birthday_arg"
        internal const val ARG_PREFIX = "prefix_arg"
        internal const val ARG_ISNIE = "isnie_arg"
        internal const val ARG_REFRESH = "refresh_arg"
        internal const val ARG_IDENTIFIER = "identifier_arg"
        internal const val ARG_SAVE_USER = "saveuser_arg"
        internal const val ARG_FIRST_ACCESS = "key_first_access"
        internal const val ARG_PERMISSION_NAVIGATION = "key_permission_navigation"
        internal const val ARG_ON_BOARDING_FIRST_ACCESS = "key_first_access_on_boarding"
        internal const val ARG_QUIZ_RESULT = "quiz_result_arg"
        internal const val ARG_VERIFICATION = "verification_arg"
        internal const val ARG_VALIDATE_QR_TOKEN = "token"
        internal const val ARG_TYPE_CERT = "type_cert"
        internal const val ARG_USER_TOKEN = "cert_token"
        internal const val ARG_DYNAMIC_LAYOUT = "dinamyc_layout"
        internal const val BUNDLE_CERT_TYPE = "bundle_cert_type"
        internal const val BUNDLE_CERT_TITLE = "cert_title"
        internal const val BUNDLE_CERT_BENEFICIARY = "bundle_cert_beneficiary"
        internal const val ARG_PAYLOAD = "payload"
        internal const val ARG_ITEM = "item"
        internal const val ARG_QUEST_FILLED = "questFilled"
        internal const val ARG_TITLE = "title"
        internal const val ARG_ACCESS_LEVEL = "access_level"
        internal const val ARG_ID = "id"
        internal const val ARG_PENDING_NAV = "pending"
        internal const val ARG_MEASURE_HELP = "help"
        internal const val ARG_DYNAMIC_ICONS = "dynamic_icons"
        internal const val ARG_WALLET_CERT = "wallet_certificate"

        internal const val ARG_ADVICE_TYPE = "adviceType"
        internal const val ARG_ADVICE = "advice"
        internal const val ARG_ADVICE_NUHSA = "advice_nuhsa"
        internal const val ARG_ADVICE_PHONE = "advice_phone"
        internal const val ARG_ADVICES_SHARED = "advices_shared"
        internal const val ARG_HAVE_TO_NAVIGATE_HOME = "need_navigate_home"
        internal const val ARG_ADVICES_NAVIGATION_ENTITY = "advices_navigation_entity"

        internal const val ARG_DYN_QUEST_AVAILIBILITY = "dynamic_quizzes_availability"
        internal const val ARG_QUIZ_ID = "quiz_id"
        internal const val ARG_QUIZ_TITLE = "quiz_title"
        internal const val ARG_QUEST_HELP = "quest_help"

        // endregion

        internal const val APP_BASE_KEY = "msspa.app.101"
        internal const val KEY_DURATION: Int = 100
        internal const val SPAIN_PREFIX = "+34"
        internal const val PLUS = "+"
        internal const val URL_PARAM = "url"
        internal const val FOLLOWUP_PARAM = "follow_up"
        internal const val PROGRAM__QUESTIONS_PARAM = "quest"
        internal const val PARAMETER_PARAM = "param"
        internal const val COVID_BACK_SUPPORT = "#covid"
        internal const val APPOINTMENTS_PACKAGE_NAME =
            "es.juntadeandalucia.msspa.appcitasandalucia.android"
        internal const val APPOINTMENTS_URI =
            "https://play.google.com/store/apps/details?id=$APPOINTMENTS_PACKAGE_NAME"
        const val HELP_LOGIN_URL =
            "https://www.sspa.juntadeandalucia.es/servicioandaluzdesalud/msspa_gateway/autenticacion/ayuda"
        internal const val SPLASH_DELAY = 2000L
        internal const val MEASURES_DELAY = 500L
        internal const val IMAGE_HEADER_TRANSITION = "image_header_transition_"
        internal const val SAS_WEB = "https://www.sspa.juntadeandalucia.es/servicioandaluzdesalud/"
        internal const val NUHSA_PREFIX = "AN"
        internal const val BIRTHDAY_FORMAT = "dd/MM/yyyy"
        internal const val BIRTHDAY_MIN_YEARS = 30
        internal const val ANSWER_YES = "si"
        internal const val ANSWER_NO = "no"
        internal const val CHART_ANIMATION_DURATION = 1000
        internal const val CHART_FILL_ALPHA = 26
        internal const val CHART_HEIGHT = 512
        internal const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 2323
        internal const val FORBIDDEN_QUESTIONNAIRE = "Forbidden questionnaire"

        // region - News
        internal const val ARGUMENT_NEWS_ENTITY = "new_entity"
        internal const val OPERATION_MODE_APP = "app"
        // endregion

        // region - Preferences
        internal const val PREF_NAME = "preferences"
        internal const val PREF_USER_LOGGED = "pref_user_logged"
        internal const val PREF_LEGAL_FIRST_ACCESS = "legal_first_access"
        internal const val PREF_PERMISSION_SCREEN_FIRST_ACCESS = "permission_screen_first_access"
        internal const val PREF_PERMISSION_NOTIFICATION_FIRST_ACCESS = "permission_notification_first_access"
        internal const val PREF_FIRST_ACCESS_ON_BOARDING = "first_access_on_boarding"
        internal const val PREF_FIRST_ACCESS_MONITORING = "first_access_monitoring"
        internal const val PREF_FIRST_ACCESS_ADVICES = "first_access_advices"
        internal const val PREF_FIRST_ACCESS_ADVICES_CATALOG_TYPE = "first_access_advices_catalog_type"
        internal const val PREF_SAVED_USERS = "pref_saved_users"
        internal const val PREF_FIRST_SAVE_USER_ADVICE = "pref_first_save_user_advice"
        internal const val PREF_FIRST_LOAD_USER_ADVICE = "pref_first_load_user_advice"
        internal const val PREF_HMS_GMS_TOKEN = "pref_firebase_token"
        internal const val PREF_DYNAMIC_RELEASES = "pref_dynamic_releases"
        internal const val PREF_NOTIFICATION_SUBSCRIPTION_PHONE_NUMBER =
            "notifications_phone_number"
        internal const val PREF_FIRST_ACCESS_TO_SCAN_CERTIFICATE = "first_access_scan"
        internal const val PREF_FIRST_ACCESS_TO_VALIDATE_CERTIFICATE = "first_access_validate"
        internal const val PREF_FIRST_ACCESS_TO_CERTIFICATE_HUB = "first_access_certificate_hub"
        internal const val PREF_FIRST_ACCESS_TO_DYN_QUEST_HUB = "first_access_dyn_quest_hub"
        internal const val PREF_USER_SESSION = "pref_user_session"
        internal const val PREF_QUIZ_SESSION = "pref_quiz_session"
        internal const val PREF_TRUSTLIST = "pref_trustlist"
        internal const val PREF_FIRST_LOAD_WALLET = "pref_wallet"
        internal const val WALLET_DYNAMIC_ACTIVATED ="pref_wallet_activated"
        internal const val PREF_FIRST_TIME_CONTACT_PERMISSION_REQUEST="pref_first_time_contact_permission_request"
        internal const val PREF_CHECK_DYNAMIC_RELEASES = "check_dynamic_releases"
        internal const val PREF_LIKE_IT = "pref_like_it"
        // endregion

        internal const val KEY_SAVED_USERS = "key_pref_saved_users"
        internal const val IDENTIFIER = "YWxtYWNlbl9jZXJ0aWZpY2Fkb3NfY292aWQ="

        internal const val MAX_SPAIN_PHONE_NUMBER_LENGTH = 9
        internal const val MAX_NUHSA_LENGTH = 12
        internal const val MAX_IDENTIFIER_LENGTH = 9
        internal const val TIME_TO_APPEAR_ADVICE = 1500L
        internal const val TIME_TO_DISAPPEAR_ADVICE = 8000L

        // region - Validations
        internal const val VALIDATION_MIN_LENGTH_NUSHA = 12
        internal const val VALIDATION_MAX_LENGTH_NUSHA = 12
        internal const val VALIDATION_MIN_LENGTH_BIRTHDAY = 10
        internal const val VALIDATION_MIN_LENGTH_IDENTIFIER = 9
        internal const val VALIDATION_MIN_LENGTH_SPANISH_PHONE = 9
        internal const val VALIDATION_MAX_LENGTH_SPANISH_PHONE_NUMBER = 9
        internal const val VALIDATION_MAX_DAYS_CERTIFICATE = 270
        internal const val DAY_MILLISECONDS = 24 * 60 * 60 * 1000

        // endregion

        // region - featured
        internal const val FEATURE_COVID = "covid"
        internal const val FEATURE_PREFERENCES = "preferencias"
        internal const val FEATURE_COVID_CERT = "certvac"
        internal const val FEATURE_OP_APP = "app"
        internal const val FEATURE_OP_WEBVIEW = "webview"
        internal const val FEATURE_OP_EXTERNAL = "external"
        internal const val FEATURE_HUB_CERT = "certvac_eu"

        internal const val CAMERA_PERMISSION_REQUEST_CODE = 100

        internal const val VACCINE_CERT_QR_TEMPLATE =
            BuildConfig.API_BASE_HOST + "/cervac?app=msspa.app.102&func=validate_qr&token="

        // region - Dynamic Menu
        internal const val CLIC_SALUD_MENU = 110
        //endregion

        // JWT
        internal const val JWT_IDENTIFIER = "id"
        internal const val JWT_VACCINE_DATE = "fechaVacunacion"

        // region Measurements
        internal const val MEASURE_TYPE = "tipo"
        internal const val MEASURE_SUBTYPE = "subtipo"
        internal const val MEASURE_UNIT = "unit"
        internal const val MEASURE_CURRENT_PAGE = "self"
        internal const val MEASURE_LAST_PAGE = "last"
        internal const val MEASURE_HELPER_RANGE = "Rango"
        internal const val MEASURE_HELPER_INFO = "Info"
        internal const val MEASURE_DELIMITER_PAGE = "page="
        internal const val MEASURE_DELIMITER_AMPERSAND = "&"
        //endregion

        // region - covid cert - greenpass - user
        internal const val CERT_NAME_AREA = "nam"
        internal const val USER_SURNAME = "fn"
        internal const val USER_NAME = "gn"
        internal const val USER_BIRTHDATE = "dob"
        internal const val USER_ID_PARAM = "id"
        // endregion

        // region - andalusian covid cert
        internal const val TYPE_CONTRAINDICATIONS = "cervac"
        internal const val CERT_NAME = "nombre"
        internal const val CERT_FIRST_SURNAME = "primerApellido"
        internal const val CERT_SECOND_SURNAME = "segundoApellido"
        internal const val CERT_VACCINE_DATE = "fechaVacunacion"
        // endregion

        // region - covid cert - greenpass - cert
        internal const val QR = "qr"
        internal const val DATE_VACCINATION_PARAM = "dt"
        internal const val TOTAL_VACCINES = "sd"
        internal const val VACCINE_NAME = "mp"
        internal const val VACCINE_NUMBER = "dn"

        internal const val RECOVERY_STAUS = "fr"
        internal const val RECOVERY_DATE = "du"

        internal const val NEGATIVE_TEST_DATE = "dr"
        internal const val TEST_TYPE = "tt"

        internal const val TYPE_VACCINATION = "vacunacion"
        internal const val TYPE_TEST = "pruebas"
        internal const val TYPE_RECOVERY = "recuperacion"
        internal const val TYPE_VACCINATION_SHORT = "v"
        internal const val TYPE_TEST_SHORT = "t"
        internal const val TYPE_RECOVERY_SHORT = "r"

        internal const val VACCINE_CERT_IMG =
            "https://ws238.sspa.juntadeandalucia.es:9443/images/image96"
        internal const val RECOVERY_CERT_IMG =
            "https://ws238.sspa.juntadeandalucia.es:9443/images/image95"
        internal const val TEST_CERT_IMG =
            "https://ws238.sspa.juntadeandalucia.es:9443/images/image94"

        internal const val VACCINE_CERT_IMG_PRE =
            "https://ws237.sspa.juntadeandalucia.es:9443/images/image111"
        internal const val RECOVERY_CERT_IMG_PRE =
            "https://ws237.sspa.juntadeandalucia.es:9443/images/image110"
        internal const val TEST_CERT_IMG_PRE =
            "https://ws237.sspa.juntadeandalucia.es:9443/images/image109"

        internal const val FORMAT_PDF = "Print"
        internal const val SPLIT_URI = "target="
        internal const val SPLIT_URI_DYNAMIC = "target=dynamic_screen"
        internal const val STRING_FORMAT_SELECTED_OPTION = "opcionSeleccionada="
        internal const val STRING_FORMAT_SCREEN="screen="
        internal const val VERIFICATION_CERT = "ccd_verification"
        internal const val TOKEN_PARAMETER = "token"
        internal const val ID_TYPE_PARAMETER ="tipoId"
        internal const val ID_VALUE_PARAMETER="id"
        // endregion

        // region get  green pass images url

        fun getGreenPassImageUrl(type: GreenPassTypeEntity): String {
            return when (type) {
                GreenPassTypeEntity.TYPE_VACCINATION -> if (BuildConfig.FLAVOR.contains(BUILD_TYPE_PRE)) VACCINE_CERT_IMG_PRE else VACCINE_CERT_IMG
                GreenPassTypeEntity.TYPE_RECOVERY -> if (BuildConfig.FLAVOR.contains(BUILD_TYPE_PRE)) RECOVERY_CERT_IMG_PRE else RECOVERY_CERT_IMG
                GreenPassTypeEntity.TYPE_TEST -> if (BuildConfig.FLAVOR.contains(BUILD_TYPE_PRE)) TEST_CERT_IMG_PRE else TEST_CERT_IMG
            }
        }

        // endregion

        //region -Quiz values
        const val TYPE_BOOLEAN = 1
        const val TYPE_DECIMAL = 2
        const val TYPE_TEXT = 3
        const val TYPE_OPTIONS = 4
        const val TYPE_BOOLEAN_EXT = 5
        const val TYPE_SINGLE_OPTION = 6
        const val TYPE_MULTIPLE_OPTIONS = 7
        // endregion

        //region FollowUp
        const val TYPE_FOLLOW_UP = "followup"
        const val TYPE_MONITORING_PROGRAM = "CarePlan"
        const val TYPE_MEASUREMENT = "measurement"
        const val TYPE_HELP = "AyudaMedicion"
        const val LEGEND_SQUARE_SIZE = 10f
        const val LEGEND_VALUE_BELOW = "75"
        const val LEGEND_VALUE_BETWEEN_MIN = "76"
        const val LEGEND_VALUE_BETWEEN_MAX = "100"
        const val LEGEND_VALUE_ABOVE = "100"
        //endregion

        //region Dynamic quizz
        const val QUIZ_ID_PARAM = "quiz_id"
        const val UNIT_PARAM = "unit"
        const val RESPONSE_NUM_CHOICE_PARAM = "numRespuestas"
        const val IMAGE_VALUE = "image_"
        //endregion

        //region PRIVATE FILES
        internal const val FILE_HOME = "priv_dynamic_home.json"
        internal const val FILE_MENU = "priv_dynamic_menu.json"
        internal const val FILE_SCREEN = "priv_dynamic_screen.json"
        internal const val FILE_RELEASES = "priv_dynamic_releases.json"

        internal const val TEMP_FILE = "temp."
        //endregion

        val whiteSpaceFilter =
            InputFilter { source, _, _, _, _, _ ->
                source.forEach{ if (Character.isSpaceChar(it)) return@InputFilter "" }
                null
            }

        //region Avisas
        const val PARENT_SUBSCRIPTION = "suscripcionPadre"
        const val SHARED_SUBSCRIPTION = "suscripcionCompartida"
        const val ADVICE_TYPE_ACTIVE = "active"
        const val ADVICE_TYPE_OFF = "off"
        const val FULL_URL_CONSTS = "/usuarios/aplicaciones/notificaciones/suscripciones"
        const val PREFIX_CONTACT = "phone|"
        const val MOBILE = "mobile"
        const val ADVICE_STATUS = "status"
        const val ADVICE_DIALOG_TAG = "adviceDialog"
        //endregion

        internal const val SMS_RECEIVER_CONTENT = "El código de verificación de su teléfono es"

        //region Dynamic Events
        internal const val TYPE_RESET = "reset"

        internal const val ACTION_ME_GUSTA = "me_gusta"

        internal const val TARGET_DIALOG = "dialog"

        //endregion
    }
}
