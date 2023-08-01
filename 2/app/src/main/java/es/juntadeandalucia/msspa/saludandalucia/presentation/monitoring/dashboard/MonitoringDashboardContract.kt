package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class MonitoringDashboardContract {

    interface View : BaseContract.View {
        fun setupView()
        fun showOnBoardingDialog()
        fun navigateToPrograms(accessLevel: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun setupView(accessLevel: String)
        fun onAccessButtonPressed()
    }
}