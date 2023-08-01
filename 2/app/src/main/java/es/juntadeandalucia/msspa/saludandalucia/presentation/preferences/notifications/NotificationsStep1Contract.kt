package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NotificationsStep1Contract {

    interface View : BaseContract.View {
        fun setupView()
        fun showValidPhoneNumber()
        fun showErrorPhoneNumber()
        fun showValidPhonePrefix()
        fun showErrorPhonePrefix()
        fun navigateToStep2(verificationCodeEntity: RequestVerificationCodeEntity)
        fun showInputErrorsDialog()
        fun showMaxAttemptsDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun checkPhonePrefix(prefix: String)
        fun validatePhonePrefix(prefix: String): Boolean
        fun validatePhoneNumber(prefix: String, phoneNumber: String): Boolean
        fun checkPhoneNumber(prefix: String, phoneNumber: String)
        fun onNextStepClicked(prefix: String, phone: String)
    }
}
