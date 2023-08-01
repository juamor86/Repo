package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager


class QRVerificationContract {

    interface View : BaseContract.View {
        fun setupView()
        fun showPinError()
        fun showCancelLoginPopUp()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(
            authEntity: MsspaAuthenticationEntity,
            authConfig: MsspaAuthenticationConfig
        )
        fun onSendPinClicked(pin: String, cryptoManager: CrytographyManager)
        fun onCancelClicked()
        fun onPinFailed()
        fun onLoginSuccess()
        fun cleanLoginQRandFinish()
    }

}