package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class PreferencesPresenter(
    private val getNotificationsPhoneNumberUseCase: SingleUseCase<String>,
    private val clearNotificationsSubscriptionUseCase: CompletableUseCase,
    private val getFirstAccess: GetFirstAccessUseCase,
    private val saveFirstAccess: SetFirstAccessUseCase
) :
    BasePresenter<PreferencesContract.View>(), PreferencesContract.Presenter {

    private var phoneNumber: String = ""

    override fun onResume() {
        checkNotificationsEnabled()
        view.checkPermissions()
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.PREFERENCES_SCREEN_ACCESS

    override fun onCreate() {
        if (checkFirstOpenOnBoarding()) {
            view.showOnBoarding()
            saveFirstAccess()
        }
    }

    private fun checkFirstOpenOnBoarding(): Boolean {
        return getFirstAccess.param(Consts.PREF_FIRST_ACCESS_ON_BOARDING).execute()
    }

    private fun saveFirstAccess() {
        saveFirstAccess.param(Consts.PREF_FIRST_ACCESS_ON_BOARDING).execute(onComplete = {}, onError = { Timber.e(it) })
    }

    private fun checkNotificationsEnabled() {
        getNotificationsPhoneNumberUseCase.execute(
            onSuccess = { phoneNumber ->
                if (phoneNumber.isNotEmpty()) {
                    this.phoneNumber = phoneNumber

                    with(view) {
                        enableNotificationsSwitch()
                        showNotificationsPhone(phoneNumber)
                    }
                } else {
                    view.disableNotificationsSwitch()
                }
                view.setupSwitch()
            },
            onError = {
                Timber.e("Error checking if notifications are enabled")
                view.setupSwitch()
            }
        )
    }

    override fun onNotificationsEnabled() {
        view.navigateToVerification()
    }

    override fun onNotificationsDisabled() {
        view.showConfirmDialog()
    }

    override fun onCancelDisableNotifications() {
        view.enableNotificationsSwitch()
    }

    /*override fun shouldRequestPermissionRationale() {
        view.requestContactPermission()
    }*/

    /*override fun shouldNotRequestPermissionRationale() {
        if (getFirstAccess.param(Consts.PREF_FIRST_TIME_CONTACT_PERMISSION_REQUEST).execute()) {
            saveFirstAccess.param(Consts.PREF_FIRST_TIME_CONTACT_PERMISSION_REQUEST)
                .execute(onComplete = {
                    view.requestContactPermission()
                }, onError = {
                    Timber.e(it)
                })
        } else {
            view.goToConfiguration()
        }
    }*/

    override fun disableNotifications() {
        view.showLoading()
        clearNotificationsSubscriptionUseCase.execute(
            onComplete = {
                clearPhoneNumber()
                with(view) {
                    hideLoading()
                    hideNotificationsPhone()
                }
            },
            onError = {
                Timber.e("Error disabling notifications: ${it.localizedMessage}")
                with(view) {
                    hideLoading()
                    showUnsubscriptionErrorDialog()
                    enableNotificationsSwitch()
                }
            }
        )
    }

    private fun clearPhoneNumber(){
        this.phoneNumber = ""
    }

    override fun unsubscribe() {
        getNotificationsPhoneNumberUseCase.clear()
        clearNotificationsSubscriptionUseCase.clear()
        saveFirstAccess.clear()
    }
}
