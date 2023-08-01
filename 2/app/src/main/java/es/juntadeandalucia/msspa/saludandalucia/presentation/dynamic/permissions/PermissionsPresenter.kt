package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.permissions

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.LauncherScreenBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LauncherScreen
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class PermissionsPresenter(
    private val saveFirstAccess: SetFirstAccessUseCase,
    private val getFirstAccessUseCase: GetFirstAccessUseCase,
    private val launcherScreenBus: LauncherScreenBus
) : BasePresenter<PermissionsContract.View>(), PermissionsContract.Presenter {

    override fun onViewCreated(dynamicScreenEntity: DynamicScreenEntity?) {
        view.apply {
            val isFirstAccess = getFirstAccessUseCase.param(Consts.PREF_PERMISSION_SCREEN_FIRST_ACCESS).execute()
            if (isFirstAccess) {
                hideToolbar()
            } else {
                hideCheckAndButtom()
            }
            setupViews()
            buildScreen(dynamicScreenEntity)
        }
    }

    override fun onUnderstoodClick() {
        saveFirstAccess.param(Consts.PREF_PERMISSION_SCREEN_FIRST_ACCESS).execute(
            onComplete = {
                Timber.i("Permission screen checked")
                view.closeView()
                launcherScreenBus.createLauncherScreen(LauncherScreen.LauncherScreenTypes.NOTIFICATION_PERMISSION)
            },
            onError = {
                Timber.i("Permission screen not checked")
                Timber.e(it)
            }
        )
    }

    override fun onSwitchChanged(checked: Boolean) {
        view.enableAcceptButton(checked)
    }

    override fun isActivatedPermission() {
        view.displayToolbarOrNo(!getFirstAccessUseCase.param(Consts.PREF_PERMISSION_SCREEN_FIRST_ACCESS).execute())
    }

    override fun onBackPressedEvent() {
        view.doBackOrNo(!getFirstAccessUseCase.param(Consts.PREF_PERMISSION_SCREEN_FIRST_ACCESS).execute())
    }

    override fun unsubscribe() {
    }
}
