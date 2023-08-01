package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class MeasurementsContract {

    interface View : BaseContract.View {
        fun animateView()
        fun initView(sectionEntity: List<MeasurementSectionEntity>)
        fun onSectionClicked(measure: MeasurementSectionEntity)
        fun onFilterClicked(measure: String)
        fun onGraphicButtonPressed(measure: MeasurementSectionEntity)
        fun onListButtonPressed()
        fun showDetailInfo(helpText: String?)
        fun reDrawAdapter(measures: List<MeasurementSectionEntity>)
        fun showNoMeasuresText()
    }

    interface Presenter : BaseContract.Presenter {
        fun onSectionClicked(measure: MeasurementSectionEntity)
        fun onFilterClicked(measure: String)
        fun onGraphicButtonPressed(measure: MeasurementSectionEntity)
        fun onListButtonPressed(measureTitle:String)
        fun getDetailInfo(measure: MeasurementSectionEntity)
    }
}
