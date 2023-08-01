package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.scanner

import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract

class ScanQRContract {

    interface View : BaseContract.View {
        fun configureCamera()
        fun startCameraPreview()
        fun releaseCamera()
        fun showScanErrorDialog()
        fun sendQRCode(qr: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun onPause()
        fun onQRScanned(qr: String)
        fun onScanError()
        fun onDismissedDialog()
    }
}
