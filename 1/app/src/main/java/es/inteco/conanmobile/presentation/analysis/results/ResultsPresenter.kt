package es.inteco.conanmobile.presentation.analysis.results

import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.base.BasePresenter

/**
 * Results presenter
 *
 * @constructor Create empty Results presenter
 */
class ResultsPresenter : BasePresenter<ResultsContract.View>(), ResultsContract.Presenter {

    lateinit var result: AnalysisResultEntity
    var previousAnalysis: AnalysisResultEntity? = null

    override fun onViewCreated(
        analysisResultEntity: AnalysisResultEntity,
        previousAnalysis: AnalysisResultEntity?
    ) {
        this.result = analysisResultEntity
        this.previousAnalysis = previousAnalysis
        view.toolbarTittle()
        view.initButtons()
        processResultsToShow()
    }

    override fun onDeviceClicked() {
        view.navigateDetailResult(previousAnalysis, result, ModuleEntity.AnalysisType.SETTING)
    }

    override fun onAppsClicked() {
        view.navigateToApps()
    }

    override fun onPermissionClicked() {
        view.navigateDetailResult(previousAnalysis, result, ModuleEntity.AnalysisType.SYSTEM)
    }

    private fun processResultsToShow() {
        with(result) {
            var items = 0
            deviceItems.forEach {
                if (it.notOk) {
                    items++
                }
            }
            view.loadResults(items, appsItems.filter { it.isMalicious == 1 || it.criticalPermissions.isNotEmpty() }.size, systemItems.size)
        }
    }

    override fun onNavigateToOSIClicked() {
        view.navigateOSITips()
    }

    override fun onNavigateToWhatsapp() {
        view.navigateToWhatsapp()
    }

    override fun whatsappNotInstalled() {
        view.showWarningIntentWhatsapp()
    }
}