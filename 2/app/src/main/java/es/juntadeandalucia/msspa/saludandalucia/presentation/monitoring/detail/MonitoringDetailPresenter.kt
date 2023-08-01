package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class MonitoringDetailPresenter() : BasePresenter<MonitoringDetailContract.View>(),
    MonitoringDetailContract.Presenter {

    override fun getScreenNameTracking(): String? = Consts.Analytics.FOLLOWUP_DETAIL_ACCESS

    override fun onViewCreated(questFilled: MonitoringListEntity.QuestFilledEntity,id:String) {
        view.apply {
            sendEvent(Consts.Analytics.FOLLOWUP_DETAIL_ACCESS)
            setupView(questFilled)
        }
    }

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
    }
}
