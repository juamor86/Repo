package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.pin

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import javax.crypto.Cipher

class PinContract {

    interface View : BaseContract.View {
        fun setupView()
        fun showHelp()
        fun askForPinConfirmation()
        fun navigateBack()
        fun onNavBackPressed()
        fun navigateOnBackPressed(confirmScreen:Boolean, firstPin: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun onHelpClicked()
        fun onContinueClick(pin: String)
        fun onViewCreated(authEntity: MsspaAuthenticationEntity)
        fun endLoginProcess(pin: String)
        fun performNavBackPressed()
        fun setConfirmScreen(confirmScreen:Boolean)
    }
}
