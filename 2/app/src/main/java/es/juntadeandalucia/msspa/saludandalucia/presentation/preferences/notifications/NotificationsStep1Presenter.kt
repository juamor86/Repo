package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import androidx.core.text.isDigitsOnly
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.VerificationMaxAttemptsExceededException
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.RequestVerificationCodeUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.VALIDATION_MIN_LENGTH_SPANISH_PHONE
import timber.log.Timber

class NotificationsStep1Presenter(private val requestVerificationCodeUseCase: RequestVerificationCodeUseCase) :
    BasePresenter<NotificationsStep1Contract.View>(),
    NotificationsStep1Contract.Presenter {

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.setupView()
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.NOTIFICATION_STEP1_SCREEN_ACCESS

    override fun unsubscribe() {
        requestVerificationCodeUseCase.clear()
    }

    override fun checkPhoneNumber(prefix: String, phoneNumber: String) {
        if (Consts.SPAIN_PREFIX == prefix) {
            if (phoneNumber.length >= VALIDATION_MIN_LENGTH_SPANISH_PHONE) {
                validatePhoneNumber(prefix, phoneNumber)
            }
        }
    }

    override fun checkPhonePrefix(prefix: String) {
        view.showValidPhonePrefix()
    }

    override fun validatePhonePrefix(prefix: String): Boolean =
        if (prefix.isNotBlank().and(prefix.contains(Consts.PLUS).and(prefix.length > 1))) {
            view.showValidPhonePrefix()
            true
        } else {
            view.showErrorPhonePrefix()
            false
        }

    override fun validatePhoneNumber(prefix: String, phoneNumber: String): Boolean =
        if (phoneNumber.isNotBlank() && phoneNumber.isDigitsOnly()) {
            if (Consts.SPAIN_PREFIX != prefix || (Consts.SPAIN_PREFIX == prefix && phoneNumber.length == 9)) {
                view.showValidPhoneNumber()
                true
            } else {
                view.showErrorPhoneNumber()
                false
            }
        } else {
            view.showErrorPhoneNumber()
            false
        }

    override fun onNextStepClicked(prefix: String, phone: String) {
        val validPhonePrefix = validatePhonePrefix(prefix)
        val validPhone = validatePhoneNumber(prefix, phone)
        if (validPhonePrefix && validPhone) {
            view.showLoading()
            requestVerificationCodeUseCase.params("$prefix$phone").execute(
                onSuccess = { requestVerificationCodeEntity ->
                    view.apply {
                        hideLoading()
                        navigateToStep2(requestVerificationCodeEntity)
                    }
                },
                onError = {
                    Timber.e("Error getting the verification code: ${it.message}")
                    view.hideLoading()
                    when (it) {
                        is TooManyRequestException -> view.showTooManyRequestDialog()
                        is VerificationMaxAttemptsExceededException -> view.showMaxAttemptsDialog()
                        else -> view.showErrorDialog()
                    }
                }
            )
        } else {
            view.showInputErrorsDialog()
        }
    }
}
