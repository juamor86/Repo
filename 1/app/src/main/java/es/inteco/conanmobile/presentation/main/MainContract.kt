package es.inteco.conanmobile.presentation.main

import android.content.Context
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Main contract
 *
 * @constructor Create empty Main contract
 */
class MainContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Init controls with default analysis
         *
         * @param analysisEntity
         */
        fun initControlsWithDefaultAnalysis(analysisEntity: AnalysisEntity)

        /**
         * Navigate to external
         *
         * @param url
         */
        fun navigateToExternal(url: String)

        /**
         * Navigate to analysis
         *
         * @param analysisEntity
         */
        fun navigateToAnalysis(analysisEntity: AnalysisEntity)

        /**
         * Error launched analysis
         *
         * @param message
         */
        fun errorLaunchedAnalysis(message: String)

        /**
         * Show close message
         *
         */
        fun showCloseMessage()

        /**
         * Close
         *
         */
        fun close()

        /**
         * Navigate to results
         *
         * @param result
         */
        fun navigateToResults(result: AnalysisResultEntity)

        /**
         * Show analysis unavailable message
         *
         * @param message
         */
        fun showAnalysisUnavailableMessage(message: String)

        /**
         * Show view last analysis
         *
         * @param visible
         */
        fun showViewLastAnalysis(visible: Boolean)

        /**
         * Show analysis running
         *
         */
        fun showAnalysisRunning()

        /**
         * Show enable gps message
         *
         */
        fun showEnableGpsMessage()

        /**
         * Navigate to whatsapp
         *
         */
        fun navigateToWhatsapp()

        /**
         * Navigate to telegram
         *
         */
        fun navigateToTelegram()

        /**
         * Show warning intent whatsapp
         *
         */
        fun showWarningIntentWhatsapp()

        /**
         * Is location permitted
         *
         * @return
         */
        fun isLocationPermitted(): Boolean

        /**
         * Request location permission
         *
         */
        fun requestLocationPermission()

        /**
         * Navigate to legal screen
         *
         * @param message
         */
        fun navigateToLegalScreen(message: MessageEntity)

        /**
         * Navigate to help screen
         *
         */
        fun navigateToHelpScreen()

        /**
         * Deactivate analysis until
         *
         * @param time
         */
        fun deactivateAnalysisUntil(time: Long)

        /**
         * Show alert recommended analysis
         *
         */
        fun showAlertRecommendedAnalysis()

        /**
         * Show pending warnings
         *
         * @param haveNotifications
         */
        fun showPendingWarnings(haveNotifications: Boolean)
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * Go to device configuration
         *
         * @param ct
         */
        fun goToDeviceConfiguration(ct: Context)

        /**
         * Lunch whatsapp
         *
         * @param ct
         */
        fun lunchWhatsapp(ct: Context)

        /**
         * On start analysis clicked
         *
         */
        fun onStartAnalysisClicked()

        /**
         * On location permission granted
         *
         */
        fun onLocationPermissionGranted()

        /**
         * On last analysis clicked
         *
         */
        fun onLastAnalysisClicked()

        /**
         * On navigate up
         *
         */
        fun onNavigateUp()

        /**
         * On legal clicked
         *
         */
        fun onLegalClicked()

        /**
         * On help clicked
         *
         */
        fun onHelpClicked()
    }
}