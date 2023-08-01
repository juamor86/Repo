package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import es.juntadeandalucia.msspa.saludandalucia.R

object DynamicConsts {

    object UI {
        private val LAYOUT_GRID_3 = "grid_3" to R.layout.layout_dyn_3
        private val LAYOUT_GRID_4 = "grid_4" to R.layout.layout_dyn_4
        private val LAYOUT_GRID_5 = "grid_5" to R.layout.layout_dyn_5
        private val LAYOUT_GRID_6 = "grid_6" to R.layout.layout_dyn_6
        private val LAYOUT_BUTTONS_2 = "buttons_2" to R.layout.layout_dyn_buttons
        private val LAYOUT_BUTTONS_3 = "buttons_3" to R.layout.layout_dyn_buttons
        private val ELEM_IMG = "elem_img" to R.layout.item_dyn_img
        private val ELEM_TITLE = "elem_t" to R.layout.item_dyn_t
        private val ELEM_VERT_IMG_TITLE = "elem_v_img_t" to R.layout.item_dyn_v_img_t
        private val ELEM_HORIZ_IMG_TITLE = "elem_h_img_t" to R.layout.item_dyn_h_img_t
        private val ELEM_VERT_IMG_TITLE_TXT = "elem_v_img_t_tx" to R.layout.item_dyn_v_img_t_tx
        private val ELEM_HORIZ_IMG_TITLE_TXT = "elem_h_img_t_tx" to R.layout.item_dyn_h_img_t_tx
        private val ELEM_BUTTON = "elem_button" to R.layout.item_dyn_button

        private val dynamicMap: HashMap<String, Int> = hashMapOf(
            LAYOUT_GRID_3,
            LAYOUT_GRID_4,
            LAYOUT_GRID_5,
            LAYOUT_GRID_6,
            LAYOUT_BUTTONS_2,
            LAYOUT_BUTTONS_3,
            ELEM_IMG,
            ELEM_TITLE,
            ELEM_VERT_IMG_TITLE,
            ELEM_HORIZ_IMG_TITLE,
            ELEM_VERT_IMG_TITLE_TXT,
            ELEM_HORIZ_IMG_TITLE_TXT,
            ELEM_BUTTON
        )

        fun getDynamicElem(jsonId: String): Int? =
            dynamicMap[jsonId]
    }

    object Releases{
        const val DYNAMIC_RELEASES_ID = "new_releases"
        const val DYNAMIC_RELEASES_CHECK_ID = "has_to_show_no_more_check"
    }

    object Nav {

        const val TARGET: String = "target"
        const val APP: String = "app"
        const val APP_NATIVE: String = "app_native"
        const val WEBVIEW: String = "webview"
        const val WEBVIEW_SESSION = "webview_session"
        const val EXTERNAL: String = "external"
        const val DYNAMIC_SCREEN = "dynamic_screen"
        const val SCREEN = "screen"
        const val CLICSALUD = "clicsalud"
        const val ACCESS_LEVEL = "access_level"

        const val ADVICE = "avisas"
        // TODO CHANGE IT TO "wallet" when service is ready

        const val WALLET = "wallet"
        const val ACCESS_PERMISSION_INFO = "access_permission_info"

        private val DEST_ABOUT = "about" to R.id.about_dest
        private val DEST_HELP = "help" to R.id.help_dest
        val DEST_HOME = "home" to R.id.home_dest
        private val DEST_PREFERENCES = "preferences" to R.id.preferences_dest
        private val DEST_NOTIFICATIONS = "notifications" to R.id.notifications_dest
        private val DEST_APPS = "apps" to R.id.apps_dest
        private val DEST_NEWS = "news" to R.id.news_dest
        private val DEST_COVID_QUIZ = "covid" to R.id.login_quiz_dest
        private val DEST_CONTRAINDICATION_CERT = "cervac" to R.id.covid_cert_dest
        private val DEST_LEGAL = "legal" to R.id.legal_dest
        private val DEST_VALIDATE_COVID_CERT = "certva" to R.id.scan_qr_dest
        private val DEST_GREEN_PASS = "certvac_hub" to R.id.hub_certificate_dest
        private val DEST_RECOVERY_CERT = "recuperacion" to R.id.certificate_detail_dest
        private val DEST_TEST_CERT = "pruebas" to R.id.certificate_detail_dest
        private val DEST_VACCINE_CERT = "vacunacion" to R.id.certificate_detail_dest
        private val DEST_CCD_VERIFY = "ccd_verification" to R.id.scan_qr_dest
        private val DEST_MONITORING_DASH = "follow_up" to R.id.monitoring_program_on_boarding_dest
        private val DEST_WALLET = "wallet" to R.id.wallet_dest

        private val DEST_ADVICE = "avisas" to R.id.advice_dest
        val DEST_GENERIC_QUESTIONAIRES = "quizzes_landing" to R.id.dyn_quest_dashboard_dest

        private val DEST_AVISAS = "avisas" to R.id.preferences_dest
        val DEST_DYNAMIC_QUIZZES_QUESTIONAIRES = "dynamic_quizzes_list" to R.id.dyn_quest_list_dest
        val DEST_MONITORING = "in_app_monitoring" to R.id.follow_up_dest
        val DEST_PERMISSION_INFORMATION = "access_permission_info" to R.id.access_permission_dest


        private val dynamicNavigationMap: HashMap<String, Int> = hashMapOf(
            DEST_ABOUT,
            DEST_PREFERENCES,
            DEST_HELP,
            DEST_HOME,
            DEST_NOTIFICATIONS,
            DEST_APPS,
            DEST_NEWS,
            DEST_COVID_QUIZ,
            DEST_LEGAL,
            DEST_VALIDATE_COVID_CERT,
            DEST_GREEN_PASS,
            DEST_VACCINE_CERT,
            DEST_TEST_CERT,
            DEST_RECOVERY_CERT,
            DEST_CCD_VERIFY,
            DEST_MONITORING_DASH,
            DEST_MONITORING,
            DEST_CONTRAINDICATION_CERT,
            DEST_WALLET,
            DEST_PERMISSION_INFORMATION,
            DEST_ADVICE,
            DEST_GENERIC_QUESTIONAIRES,
            DEST_DYNAMIC_QUIZZES_QUESTIONAIRES
        )

        fun getAppSectionDest(jsonId: String): Int? =
            dynamicNavigationMap[jsonId]
    }
}
