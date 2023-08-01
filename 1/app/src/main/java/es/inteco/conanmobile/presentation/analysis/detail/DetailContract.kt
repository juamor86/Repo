package es.inteco.conanmobile.presentation.analysis.detail

import android.content.Context
import android.graphics.drawable.Drawable
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Detail contract
 *
 * @constructor Create empty Detail contract
 */
class DetailContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Refill recicler view
         *
         * @param list
         */
        fun refillReciclerView(list: List<String>)

        /**
         * Fill toolbar tittle
         *
         * @param title
         */
        fun fillToolbarTittle(title: String?)

        /**
         * Fill sub title
         *
         * @param subTitle
         */
        fun fillSubTitle(subTitle: String?)

        /**
         * Fill issue icon
         *
         * @param icon
         */
        fun fillIssueIcon(icon: Drawable)

        /**
         * Init buttons
         *
         */
        fun initButtons()

        /**
         * Navigate to whatsapp
         *
         */
        fun navigateToWhatsapp()

        /**
         * Show warning intent whatsapp
         *
         */
        fun showWarningIntentWhatsapp()

        /**
         * Navigate to o s i tips
         *
         */
        fun navigateToOSITips()

        /**
         * Navitate to wifi settings
         *
         */
        fun navitateToWifiSettings()
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * On create view
         *
         * @param title
         * @param detailData
         */
        fun onCreateView(title: String?, detailData: MutableList<NetworkEntity>)

        /**
         * Go to device configuration
         *
         */
        fun goToDeviceConfiguration()

        /**
         * Lunch whatsapp
         *
         * @param ct
         */
        fun lunchWhatsapp(ct: Context)

        /**
         * On navigate to o s i tips clicked
         *
         */
        fun onNavigateToOSITipsClicked()
    }
}