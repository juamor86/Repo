package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract

class LoginQRContract {

    interface View : BaseContract.View {
        fun setupView()
        fun showQRHelp()
        fun launchQRReader()
        fun showErrorIdentifier()
        fun showValidIdentifier()
        fun showErrorNIEIdentifier()
        fun showErrorDNIIdentifier()
        fun showErrorNuhsa()
        fun showValidNuhsa()
        fun navigateToPin(authEntity: MsspaAuthenticationEntity)
        fun checkCameraPermission()
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(authorizeEntity: AuthorizeEntity)
        fun onQRHelpClicked()
        fun onQRButtonClicked()
        fun onLoginButtonClicked(qrCode: String, idType: String, id: String)
        fun checkIdentifier(key: String, id: String)
        fun validateIdentifier(key: String, id: String): Boolean
        fun onIdentifierTypeSelected(key: String, id: String)
    }
}
