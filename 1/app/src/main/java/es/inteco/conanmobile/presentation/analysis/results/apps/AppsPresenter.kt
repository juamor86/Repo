package es.inteco.conanmobile.presentation.analysis.results.apps

import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import es.inteco.conanmobile.presentation.base.BasePresenter
import timber.log.Timber

/**
 * Apps presenter
 *
 * @property controller
 * @property getConfigurationUseCase
 * @constructor Create empty Apps presenter
 */
class AppsPresenter(
    private val controller: AnalysisController, val getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>
) : BasePresenter<AppsContract.View>(), AppsContract.Presenter {

    private enum class LastView {
        /**
         * Malicious
         *
         * @constructor Create empty Malicious
         */
        MALICIOUS,

        /**
         * Permission
         *
         * @constructor Create empty Permission
         */
        PERMISSION
    }

    private var permissionList: List<ApplicationEntity> =
        controller.result.appsItems.filter { it.criticalPermissions.isNotEmpty() }
    private var maliciousList: List<ApplicationEntity> =
        controller.result.appsItems.filter { it.isMalicious == 1 }

    lateinit var configurationEntity: ConfigurationEntity
    private var lastView = LastView.MALICIOUS

    override fun onViewCreated() {
        view.showLoading()
        getConfigurationUseCase.execute(onSuccess = {
            configurationEntity = it
            view.hideLoading()
            initScreen()
        }, onError = {
            view.hideLoading()
            Timber.e(it, "Error al obtener la configuración en AppsPresenter")
        })
    }

    private fun initScreen() {
        view.setTitleApplications()
        view.setAppsAnalyzed(controller.result.appsItems.size)
        if (maliciousList.isEmpty() && permissionList.isEmpty()) {
            view.setDotColorGreen()
            view.showEmptyView()
        } else {
            view.setDotColorRed()
            if(lastView == LastView.MALICIOUS) {
                showMaliciousList()
            }else{
                showPermissionList()
            }
        }
    }

    override fun onMaliciousClicked() {
        lastView = LastView.MALICIOUS
        showMaliciousList()
    }

    override fun onPermissionClicked() {
        lastView = LastView.PERMISSION
        showPermissionList()
    }

    private fun showMaliciousList() {
        view.hideOk()
        view.showMaliciousButtonPressed()
        view.setSubtitle(configurationEntity.message.maliciousAppsDescription)
        view.fillMaliciousList(maliciousList)
    }

    private fun showPermissionList() {
        view.hideOk()
        view.showAttentionButtonPressed()
        view.hideSubtitle()
        view.fillPermissionList(permissionList)
    }

    override fun onMaliciousAppClicked(app: ApplicationEntity) {
        // Does nothing
    }

    override fun onPermissionAppClicked(app: ApplicationEntity) {
        view.showApp(app)
    }

}