package es.inteco.conanmobile.presentation.analysis.results.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.presentation.base.BasePresenter
import es.inteco.conanmobile.utils.AnalysisConsts

/**
 * Settings presenter
 *
 * @property controller
 * @constructor Create empty Settings presenter
 */
class SettingsPresenter(
    private val controller: AnalysisController
) : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {
    lateinit var result: AnalysisResultEntity
    var incidencesList: MutableList<ModuleResultEntity> = mutableListOf()
    var attentionList: MutableList<ModuleResultEntity> = mutableListOf()
    private var previousAnalysis: AnalysisResultEntity? = null
    override fun onCreateView(previous: AnalysisResultEntity?, result: AnalysisResultEntity, type: ModuleEntity.AnalysisType) {
        this.result = result
        this.previousAnalysis = previous
        processComparation()
        when (type) {
            ModuleEntity.AnalysisType.SETTING -> resultSettings()
            ModuleEntity.AnalysisType.APPLICATION -> resultApplication()
            ModuleEntity.AnalysisType.SYSTEM -> resultPermissions()
        }
    }

    private fun processComparation() {
        previousAnalysis?.let {
            compare(it.deviceItems, result.deviceItems)
            compare(it.systemItems, result.systemItems)
        }
    }

    private fun compare(
        previousItems: MutableList<ModuleResultEntity>, newItems: MutableList<ModuleResultEntity>
    ) {
        newItems.forEach { newItem ->
            getItemFromList(newItem, previousItems)?.let {
                if (newItem.notOk && !it.notOk) {
                    newItem.previousAnalysisDifferent = true
                }
            }
        }
    }

    private fun getItemFromList(
        item: ModuleResultEntity, items: MutableList<ModuleResultEntity>
    ): ModuleResultEntity? {
        return items.firstOrNull { it.item.code == item.item.code }
    }


    private fun resultSettings() {
        if (!result.deviceItems.isEmpty()) {
            view.hideOkScreen()
            incidencesList.clear()
            attentionList.clear()
            result.deviceItems.forEach { moduleEntity ->
                if (moduleEntity.notOk) {
                    if (moduleEntity.item.assessment.criticality == "CRITICAL") {
                        incidencesList.add(moduleEntity)
                    } else {
                        attentionList.add(moduleEntity)
                    }
                }
            }
            view.refillRecyclerView(incidencesList, attentionList)
        }
        view.setToolbarTitleSettings()
    }

    private fun resultPermissions() {
        if (!result.systemItems.isEmpty()) {
            view.hideOkScreen()
            incidencesList.clear()
            attentionList.clear()
            view.refillRecyclerView(incidencesList, attentionList)
        }
        view.setTitleSystem()
    }

    private fun resultApplication() {
        if (!result.appsItems.isNullOrEmpty()) {
            view.hideOkScreen()
            incidencesList.clear()
            attentionList.clear()
        }
        view.setTitleApplications()
    }

    override fun goToUnknownResourcesDeviceConfiguration(ct: Context) {
        ct.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
    }

    override fun onModuleClicked(module: ModuleResultEntity) {
        view.showDescription(module)
    }

    override fun onActionModuleClicked(
        module: ModuleResultEntity, view: AnalysisResultView
    ) {
        if (module.item.code == AnalysisConsts.APP_MODULE_WIFI) {
            this.view.navigateToAnalysisDetail(
                module.item.names.first().value, module.data as MutableList<NetworkEntity>
            )
        } else {
            controller.executeActionFor(module.item, view)
        }
    }
}