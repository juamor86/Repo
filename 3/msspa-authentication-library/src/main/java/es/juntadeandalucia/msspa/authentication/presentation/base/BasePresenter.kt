package es.juntadeandalucia.msspa.authentication.presentation.base

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationError
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import es.juntadeandalucia.msspa.authentication.utils.exceptions.*
import retrofit2.HttpException
import timber.log.Timber
import javax.security.cert.CertificateException
import javax.security.cert.CertificateExpiredException

abstract class BasePresenter<V : BaseContract.View> : BaseContract.Presenter {

    open lateinit var view: V

    override fun setViewContract(baseFragment: BaseContract.View) {
        view = baseFragment as V
    }

    fun handleCommonErrors(exception: Throwable) {
        Timber.e(exception)
        view.apply {
            hideLoading()
            when (exception) {
                is HttpException -> {
                    if (exception.code() >= 400) {
                        view.showError(MsspaAuthenticationError.INVALID_REQUEST)
                    } else {
                        view.setResultError(MsspaAuthenticationError.GENERIC_ERROR)
                    }
                }
                is TooManyRequestException -> {
                    view.showError(MsspaAuthenticationError.RATE_LIMIT_EXCEEDED)
                }
                is VerificationMaxAttemptsExceededException -> {
                    view.showError(MsspaAuthenticationError.MAX_ATTEMPTS)
                }
                is LoginRequiredException -> {
                    view.showWarning(MsspaAuthenticationWarning.INVALID_BDU_DATA)
                }
                is LoginPhoneRequiredException -> {
                    view.showWarning(MsspaAuthenticationWarning.INVALID_BDU_PHONE)
                }
                is InvalidPINSMSReceivedException -> {
                    view.showError(MsspaAuthenticationError.INVALID_PIN_SMS_RECEIVED)
                }
                is ProtectedUserException -> {
                    view.showWarning(MsspaAuthenticationWarning.PROTECTED_USER)
                }
                is NotPrivateHealthCareException -> {
                    view.showWarning(MsspaAuthenticationWarning.NOT_PRIVATE_HEALTH_CARE)
                }
                is LoginInvalidRequestException -> {
                    view.showError(MsspaAuthenticationError.INVALID_REQUEST)
                }
                is QRLoginNotFoundException -> {
                    view.showWarning(MsspaAuthenticationWarning.QR_LOGIN_NOT_FOUND_WARNING)
                }
                is QRLoginExpiredException -> {
                    view.showWarning(MsspaAuthenticationWarning.QR_LOGIN_EXPIRED_WARNING)
                }

                is CertificateExpiredException -> {
                    view.showError(MsspaAuthenticationError.DNIE_CERT_EXPIRED)
                }

                is CertificateException -> {
                    view.showError(MsspaAuthenticationError.DNIE_GENERIC_ERROR)
                }
                is SendNotificationErrorException -> {
                    view.showError(MsspaAuthenticationError.SEND_NOTIFICATION_ERROR)
                }
                else -> {
                    view.showError(MsspaAuthenticationError.GENERIC_ERROR)
                }
            }
        }
    }
}
