package es.juntadeandalucia.msspa.saludandalucia.presentation.wallet

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import javax.crypto.Cipher

class WalletContract {

    interface View: BaseContract.View {
        fun setupView()
        fun showDeniedAccessText()
        fun authenticationForDecrypt(
            onSuccess: (Cipher) -> Unit,
            onError: (String) -> Unit,
            unlockEntity: UnlockEntity?
        )
        fun hideDeniedAccess()
        fun animateView()
        fun showNoCertsSave()
        fun showCerts(certs: List<WalletEntity>, title: String)
        fun showOnBoardingDialog(isFirstTime: Boolean = false)
        fun navigateToDetail(cert: WalletEntity)
        fun haveBiometricOrPin():Boolean
        fun showDialogNotPhoneSecured()
    }

    interface Presenter: BaseContract.Presenter {
        fun onCreate()
        fun getDynamicIcons() : DynamicScreenEntity
        fun allowAccess()
        fun deniedAccess()
        fun navigateToDetail(cert: WalletEntity)
        fun onBoardingClosed()
    }
}