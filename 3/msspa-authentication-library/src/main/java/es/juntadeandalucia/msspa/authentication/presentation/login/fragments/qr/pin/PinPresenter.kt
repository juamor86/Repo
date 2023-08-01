package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.pin

import android.hardware.biometrics.BiometricPrompt
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.domain.usecases.SavePinUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.SetLoggingQRAttemptsUseCase
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import timber.log.Timber

class PinPresenter(
    val savePinUseCase: SavePinUseCase,
    val navBackPressedBus: NavBackPressedBus
) :
    BasePresenter<PinContract.View>(), PinContract.Presenter {
    private var firstPin: String = ""
    lateinit var authEntity: MsspaAuthenticationEntity


    var confirmationScreen = false

    override fun onViewCreated(authEntity: MsspaAuthenticationEntity) {
        this.authEntity = authEntity
        view.setupView()

        with(navBackPressedBus) {
            execute(onNext = { navBackPressedBus->
                if(navBackPressedBus.navBackType == NavBackPressed.NavBackType.PIN_SCREEN) {
                    view.onNavBackPressed()
                }
            }, onError = {
                Timber.e(it)
            })
        }
    }

    override fun onHelpClicked() {
        view.showHelp()
    }

    override fun onContinueClick(pin: String) {
        if (pin.length == 4) {
            if (!confirmationScreen) {
                firstPin = pin
                view.askForPinConfirmation()
                confirmationScreen = true
            } else {
                if (pin != firstPin) {
                    view.showWarning(R.string.different_pin, onAccept = {})
                } else {
                    endLoginProcess(pin)
                }
            }
        } else {
            view.showWarning(R.string.missing_fields, onAccept = {})
        }
    }

    override fun endLoginProcess(pin: String) {
        view.apply {
            if (isPhoneSecured()) {
                authenticateForEncryption(
                    onSuccess = { _, _ ->
                        savePin(pin)
                    },
                    onError = {
                        Timber.e(it)
                    },onErrorInt = {
                        handleErrorBiometric(it)
                    },
                    title = R.string.msspa_auth_pin_prompt_save_title,
                    subtitle = R.string.msspa_auth_pin_prompt_save_subtitle,
                    encrypt = true,
                    keyString = ApiConstants.KeyNameCipher.KEY_SAVED_PIN,
                    isNeedCipher = false
                )
            } else {
                savePin(pin)
            }
        }
    }

    private fun savePin(pin: String) {
        savePinUseCase
            .params(pin)
            .execute(onComplete = {
                view.setResultSuccess(authEntity)
            }, onError = {
                Timber.e(it)
            })
    }

    override fun performNavBackPressed() {
        view.navigateOnBackPressed(confirmationScreen, firstPin)
    }

    override fun setConfirmScreen(confirmScreen: Boolean) {
        confirmationScreen = confirmScreen
    }

    private fun handleErrorBiometric(errorCode: Int) {
        when (errorCode) {
            BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED -> view.showWarning(
                R.string.warning_authenticate_encryption,
                onAccept = {})
            else -> {}
        }
    }

    override fun unsubscribe() {
        savePinUseCase.clear()
    }
}