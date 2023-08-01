package es.inteco.conanmobile.presentation.help

import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Help contract
 *
 * @constructor Create empty Help contract
 */
class HelpContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Show no p d f dialog
         *
         */
        fun showNoPDFDialog()

        /**
         * Open pdf from raw
         *
         * @param index
         */
        fun openPdfFromRaw(index: Int)

        /**
         * Close renderer
         *
         */
        fun closeRenderer()

        /**
         * Init view
         *
         */
        fun initView()

        /**
         * Init buttons
         *
         */
        fun initButtons()
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * On start
         *
         */
        fun onStart()

        /**
         * On stop
         *
         */
        fun onStop()

        /**
         * On previous page clicked
         *
         */
        fun onPreviousPageClicked()

        /**
         * On next page clicked
         *
         */
        fun onNextPageClicked()

        /**
         * On file error
         *
         */
        fun onFileError()
    }
}