package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.phonevalidation

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract

class PhoneValidationContract {

    interface View : BaseContract.View {
        fun setupView()
        fun showErrorPhoneNumber()
        fun navigateToSMS(authorizeEntity: AuthorizeEntity, msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity)
        fun showValidPhoneNumber()
        fun enableLoginButton()
        fun disableLoginButton()
    }

    interface Presenter : BaseContract.Presenter {
        fun onContinueClick(phone: String)
        fun onViewCreated(authEntity: MsspaAuthenticationEntity, authorizeEntity: AuthorizeEntity)
        fun checkPhoneNumber(phone: String)
        fun validatePhoneNumber(phone: String): Boolean
        fun onOtherAuthMethodClicked()
    }
}
