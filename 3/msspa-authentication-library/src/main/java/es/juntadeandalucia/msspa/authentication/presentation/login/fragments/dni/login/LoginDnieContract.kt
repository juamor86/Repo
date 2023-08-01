package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login

import android.app.Activity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import java.security.KeyStore

class LoginDnieContract {

    interface View : BaseContract.View {
        fun setupView()
        fun navigateToReadDni()
        fun authenticateInProgress()
        fun checkPhoneIsNFCCompatible(): Boolean
        fun showAlertNotNFCCompatible()
    }

    interface Presenter : BaseContract.Presenter {
        fun onContinueClicked()
        fun onDniReaded(dniKeystore: KeyStore, activity: Activity)
    }
}
