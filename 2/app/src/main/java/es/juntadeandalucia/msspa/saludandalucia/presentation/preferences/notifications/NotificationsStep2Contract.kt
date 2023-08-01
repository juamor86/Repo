package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NotificationsStep2Contract {

    interface View : BaseContract.View {
        fun setupView()
        fun showValidCode()
        fun showErrorCode()
        fun close()
        fun onMessageReceived(messageText: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun checkCode(verificationCode: String)
        fun validateCode(verificationCode: String): Boolean
        fun sendVerificationCode(verificationCode: String)
        fun setupView(verificationCodeEntity: RequestVerificationCodeEntity?)
    }
}
