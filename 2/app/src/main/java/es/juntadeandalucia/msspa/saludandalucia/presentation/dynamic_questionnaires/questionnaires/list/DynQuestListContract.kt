package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list


import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class DynQuestListContract {

    interface View : BaseContract.View {
        fun setTitle(title: String)
        fun setSubtitle(subtitle: String)
        fun setupList()
        fun showList(questionnaireList: DynQuestListEntity)
        fun navigateToNewQuestionnaire(item: DynQuestionnaireEntity?, title: String, id:String)
        fun navigateToDetail(
            detailProgram: DynQuestListEntity.QuestFilledEntity,
            title: String,
            id: String
        )

        fun showNewQuestionnaireButton(showError: Boolean = false)
        fun showNoQuestionnaire()
        fun animateView()
        fun showServiceNotAvailable()
        fun showQuestionnaireNotAuthorized()
        fun comeBackToDinamic()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(item: String, title: String)
        fun onNewQuestionnaireClicked()
        fun onItemClicked(
            detailQuestionnaire: DynQuestListEntity.QuestFilledEntity,
            itemView: android.view.View
        )
        fun onRefresh()
        fun onPause()
    }
}

