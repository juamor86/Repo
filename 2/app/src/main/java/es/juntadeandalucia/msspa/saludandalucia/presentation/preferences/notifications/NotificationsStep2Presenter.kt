package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SmsBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SubscribeNotificationsUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class NotificationsStep2Presenter(
    private val subscribeNotificationsUseCase: SubscribeNotificationsUseCase,
    private val smsBus: SmsBus
) :
    BasePresenter<NotificationsStep2Contract.View>(),
    NotificationsStep2Contract.Presenter {

    private lateinit var verificationCodeEntity: RequestVerificationCodeEntity

    override fun setupView(verificationCodeEntity: RequestVerificationCodeEntity?) {
        this.verificationCodeEntity = verificationCodeEntity!!
        view.setupView()
       /* smsBus.execute(onNext = {
            view.onMessageReceived(it)
        }, onError = {Timber.e(it)})*/
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.NOTIFICATION_STEP2_SCREEN_ACCESS

    override fun checkCode(verificationCode: String) {
        if (verificationCode.length == Consts.CODE_SMS_LENGTH) {
            validateCode(verificationCode)
        }
    }

    override fun validateCode(verificationCode: String): Boolean =
        if (verificationCode.isNotBlank() && verificationCode.length == Consts.CODE_SMS_LENGTH) {
            view.showValidCode()
            true
        } else {
            view.showErrorCode()
            false
        }

    override fun sendVerificationCode(verificationCode: String) {
        val validCode = validateCode(verificationCode)
        if (validCode) {
            view.showLoading()
            subscribeNotificationsUseCase.params(
                verificationCode,
                verificationCodeEntity.idVerification,
                verificationCodeEntity.phoneNumber
            ).execute(
                onComplete = {
                    Timber.d("Subscription successfully")
                    view.apply {
                        hideLoading()
                        close()
                    }
                },
                onError = {
                    Timber.e("Error subscribing to notifications: ${it.localizedMessage}")
                    view.apply {
                        hideLoading()
                        showErrorDialog()
                    }
                }
            )
        }
    }

    override fun unsubscribe() {
        subscribeNotificationsUseCase.clear()
        smsBus.clear()
    }
}
