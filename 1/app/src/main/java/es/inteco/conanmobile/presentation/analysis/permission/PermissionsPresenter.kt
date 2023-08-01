package es.inteco.conanmobile.presentation.analysis.permission

import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.domain.entities.PermissionEntity
import es.inteco.conanmobile.presentation.base.BasePresenter

/**
 * Permissions presenter
 *
 * @constructor Create empty Permissions presenter
 */
class PermissionsPresenter : BasePresenter<PermissionsContract.View>(),
    PermissionsContract.Presenter {

    override fun onCreateView(app: ApplicationEntity) {
        buildPermissionStringList(app.criticalPermissions)
        view.fillToolbarTittle(app.name)
    }

    private fun buildPermissionStringList(permissionList: List<PermissionEntity>) {
        val list: MutableList<String> = mutableListOf()
        permissionList.forEach {
            list.add(it.description)
        }
        view.refillReciclerView(list)
    }



}