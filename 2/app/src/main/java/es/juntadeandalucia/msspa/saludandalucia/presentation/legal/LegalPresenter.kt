package es.juntadeandalucia.msspa.saludandalucia.presentation.legal

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.LauncherScreenBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LauncherScreen
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class LegalPresenter(
    private val setFirstAccess: SetFirstAccessUseCase,
    private val launcherScreenBus: LauncherScreenBus
) : BasePresenter<LegalContract.View>(), LegalContract.Presenter {

    var navigationEntityPermission: NavigationEntity? = null

    override fun onViewCreated(isFirstAccess: Boolean) {
        view.apply {
            if (isFirstAccess) {
                hideToolbar()
            } else {
                hideCheckAndButtom()
            }
        }
    }

    override fun onUnderstoodClick() {
        setFirstAccess
            .param(Consts.PREF_LEGAL_FIRST_ACCESS)
            .execute(
                onComplete = {
                    view.closeView()
                    launcherScreenBus.createLauncherScreen(LauncherScreen.LauncherScreenTypes.PERMISSIONS_SCREEN)
                },
                onError = { Timber.e(it) }
            )
    }

    override fun onSwitchChanged(checked: Boolean) {
        view.enableAcceptButton(checked)
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.LEGAL_SCREEN_ACCESS

    override fun unsubscribe() {
        setFirstAccess.clear()
    }
}
