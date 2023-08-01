package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class MonitoringDetailContract {

    interface View : BaseContract.View {
        fun setupView(entity: MonitoringListEntity.QuestFilledEntity)
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(questFilled: MonitoringListEntity.QuestFilledEntity, id: String)
    }
}
