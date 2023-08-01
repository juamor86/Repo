package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class MonitoringDashboardPresenter(
    private val getFirstAccess: GetFirstAccessUseCase,
    private val setAccess: SetFirstAccessUseCase,
    private val sessionBus: SessionBus
) : BasePresenter<MonitoringDashboardContract.View>(),
    MonitoringDashboardContract.Presenter {

    private lateinit var accessLevel: String

    override fun getScreenNameTracking(): String? = Consts.Analytics.FOLLOWUP_INTERMEDIUM_ACCESS

    override fun setupView(accessLevel: String) {
        if (getFirstAccess.param(Consts.PREF_FIRST_ACCESS_MONITORING).execute()) {
            view.showOnBoardingDialog()
            setFirstAccess()
        }
        this.accessLevel = accessLevel
        view.setupView()
    }


    private fun setFirstAccess() {
        setAccess
            .param(Consts.PREF_FIRST_ACCESS_MONITORING)
            .execute(
                onComplete = { Timber.i("First access save successfully") },
                onError = { Timber.e(it) }
            )
    }

    private fun isUserAuthenticated(): Boolean =
        sessionBus.session.isUserAuthenticated()

    override fun onAccessButtonPressed() {
        view.navigateToPrograms(accessLevel)
    }

    override fun unsubscribe() {
        setAccess.clear()
    }
}