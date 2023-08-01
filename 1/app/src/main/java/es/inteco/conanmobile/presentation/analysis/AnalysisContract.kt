package es.inteco.conanmobile.presentation.analysis

import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Analysis contract
 *
 * @constructor Create empty Analysis contract
 */
class AnalysisContract {

    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View, AnalysisView {
        /**
         * Check bluetooth permissions
         *
         */
        fun checkBluetoothPermissions()
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * On destroy
         *
         */
        fun onDestroy()

        /**
         * Start analysis
         *
         */
        fun startAnalysis()
        fun onPermissionsGranted()
        fun onPermissionsNotGranted()
    }
}