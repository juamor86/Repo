package es.inteco.conanmobile.presentation.osi

import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * O s i contract
 *
 * @constructor Create empty O s i contract
 */
class OSIContract {
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
         * Show o s i tips
         *
         * @param list
         */
        fun showOSITips(list: List<OSIEntity>)

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
    interface Presenter : BaseContract.Presenter
}