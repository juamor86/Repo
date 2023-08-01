package es.inteco.conanmobile.presentation.analysis.permission

import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.domain.entities.PermissionEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Permissions contract
 *
 * @constructor Create empty Permissions contract
 */
class PermissionsContract {
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
         * @param permissionList
         */
        fun onCreateView(permissionList: ApplicationEntity)
    }
}