package es.inteco.conanmobile.utils

/**
 * Consts
 *
 * @constructor Create empty Consts
 */
class Consts {
    companion object {
        internal const val ONE_DAY_MILLIS: Long = 24*60*60*1000L

        internal const val SPLASH_DELAY = 3000L

        //region - Args
        internal const val ARG_RESULT = "analysisResult"
        internal const val ARG_PREVIOUS = "previousAnalysisResult"
        internal const val ARG_DETAIL_TYPE = "detailResult"
        internal const val ARG_DETAIL_DATA = "detailDataResult"
        internal const val ARG_APPLICATION = "permissionList"
        internal const val ARG_CONFIGURATION = "configuration"

        //endregion
        internal const val TEL017: String = "tel:017"
        internal const val INCIBE_NOTIFICATIONS: String = "https://www.incibe-cert.es/notificaciones"
        internal const val TELEGRAM_INCIBE: String = "https://t.me/INCIBE017"
        internal const val TELEGRAM_PACKAGE_NAME: String = "org.telegram.messenger"
        internal const val INCIBE_URL = "https://www.incibe.es/"
        internal const val OSI_URL = "https://www.osi.es/es"

        //region - Preferences
        internal const val PREF_FIRST_ACCESS = "first_access"
        internal const val PREF_DEFAULT_ANALYSIS_ID = "default_analysis_id"
        internal const val PREF_CONFIGURATION_SERVICE = "configuration_service"
        internal const val PREF_REGISTERED_DEVICE = "registered_device"
        internal const val PREF_ANALYSIS_LAUNCHED = "analysis_launched"
        internal const val PREF_NEXT_DATETIME_ANALYIS = "next_analysis"
        internal const val PREF_FIRST_ANALYSIS = "first_analysis"
        internal const val TIME_BETWEEN_ANALYSIS = "time_between_analysis"
        internal const val RECOMMENDED_TIME_BETWEEN_ANALYSIS = "recommended_time_between_analysis"
        internal const val PREF_OSI_TIPS = "pref_osi_tips"
        internal const val PREF_WARNINGS = "pref_warnings"
        internal const val LAST_TIME_ALERT = "pref_last_time_alert"


        //endregion
        internal const val PHONE_CALL_PERMISSION_REQUEST_CODE = 100
        internal const val PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION: Int = 200
        internal const val WHATSAPP_INCIBE: String = "34900116117"
        internal const val WHATSAPP_PACKAGE_NAME: String = "com.whatsapp"

        //region - Configuration keys
        internal const val MASTER_KEY_NAME: String = "encrypted_master_key"
        //endregion

        internal const val FILENAME = "help"

        internal const val MALICIOUS_MALWARE = "MALWARE"
        internal const val MALICIOUS_UNKNOWN = "UNKNOWN"
    }
}