package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class MonitoringContract {

    interface View : BaseContract.View {
        fun setupView(followUpEntity: MonitoringEntity)
        fun navigateToProgram(item: MonitoringEntity.MonitoringEntry)
        fun navigateToMeasurements()
        fun showNotMonitoringMessage()
    }

    interface Presenter : BaseContract.Presenter {
        fun onProgramClicked(
            item: MonitoringEntity.MonitoringEntry,
            itemView: android.view.View
        )
    }
}
