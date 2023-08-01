package es.inteco.conanmobile.presentation.analysis.results.apps

import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Apps contract
 *
 * @constructor Create empty Apps contract
 */
class AppsContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Fill malicious list
         *
         * @param appList
         */
        fun fillMaliciousList(appList: List<ApplicationEntity>)

        /**
         * Fill permission list
         *
         * @param appList
         */
        fun fillPermissionList(appList: List<ApplicationEntity>)

        /**
         * Set subtitle
         *
         * @param maliciousAppsDescription
         */
        fun setSubtitle(maliciousAppsDescription: String)

        /**
         * Hide subtitle
         *
         */
        fun hideSubtitle()

        /**
         * Set dot color red
         *
         */
        fun setDotColorRed()

        /**
         * Set dot color green
         *
         */
        fun setDotColorGreen()

        /**
         * Set title applications
         *
         */
        fun setTitleApplications()

        /**
         * Set apps analyzed
         *
         * @param size
         */
        fun setAppsAnalyzed(size: Int)

        /**
         * Hide ok
         *
         */
        fun hideOk()

        /**
         * Show app
         *
         * @param app
         */
        fun showApp(app: ApplicationEntity)

        /**
         * Show attention button pressed
         *
         */
        fun showAttentionButtonPressed()

        /**
         * Show malicious button pressed
         *
         */
        fun showMaliciousButtonPressed()
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * On malicious clicked
         *
         */
        fun onMaliciousClicked()

        /**
         * On permission clicked
         *
         */
        fun onPermissionClicked()

        /**
         * On malicious app click listener
         *
         * @param app
         */
        fun onMaliciousAppClicked(app: ApplicationEntity)

        /**
         * On permission app click listener
         *
         * @param app
         */
        fun onPermissionAppClicked(app: ApplicationEntity)
    }
}