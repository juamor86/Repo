package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class MonitoringListContract {

    interface View : BaseContract.View {
        fun navigateToDetail(detailProgram: MonitoringListEntity.QuestFilledEntity, title: String, id:String)
        fun showList(monitoringList: MonitoringListEntity)
        fun navigateToNewProgram(item: QuestionnaireEntity)
        fun setupList()
        fun animateView()
        fun setTitle(title: String)
        fun setSubtitle(title: String)
        fun showNewMonitoringButton()
        fun showNoMonitoring()
    }

    interface Presenter : BaseContract.Presenter {
        fun onItemClicked(detailProgram: MonitoringListEntity.QuestFilledEntity, itemView: android.view.View)
        fun onNewProgramClicked()
        fun onCreate(item: MonitoringEntity.MonitoringEntry)
        fun onRefresh()
    }
}
