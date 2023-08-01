package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class DynQuestDetailContract {

    interface View : BaseContract.View {
        fun setupView(entity: DynQuestListEntity.QuestFilledEntity)
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(questFilled: DynQuestListEntity.QuestFilledEntity, id: String)
    }
}
