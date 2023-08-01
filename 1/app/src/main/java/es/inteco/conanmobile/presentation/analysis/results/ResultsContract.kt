package es.inteco.conanmobile.presentation.analysis.results

import android.content.Context
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.base.BaseContract

class ResultsContract {
    interface View : BaseContract.View{
        fun toolbarTittle()
        fun initButtons()
        fun loadResults(settings : Int, app : Int, perm : Int)
        fun navigateDetailResult(previousAnalysis: AnalysisResultEntity?, result: AnalysisResultEntity, type: ModuleEntity.AnalysisType)
        fun navigateOSITips()
        fun navigateToWhatsapp()
        fun showWarningIntentWhatsapp()
        fun navigateToApps()
    }

    interface  Presenter : BaseContract.Presenter{
        fun onViewCreated(analysisResultEntity: AnalysisResultEntity, previousAnalysis: AnalysisResultEntity?)
        fun onDeviceClicked()
        fun onAppsClicked()
        fun onPermissionClicked()
        fun onNavigateToOSIClicked()
        fun onNavigateToWhatsapp()
        fun whatsappNotInstalled()
    }
}