package es.inteco.conanmobile.presentation.warnings

import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Warnings contract
 *
 * @constructor Create empty Warnings contract
 */
class WarningsContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Show warnings error
         *
         */
        fun showWarningsError()

        /**
         * Show warnings
         *
         * @param list
         */
        fun showWarnings(list: List<WarningEntity>)

        /**
         * Init screen
         *
         */
        fun initScreen()
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * On click accept no warnings
         *
         */
        fun onClickAcceptNoWarnings()
    }
}