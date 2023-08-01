package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.scanner

import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter

class ScanQRPresenter(

) :
    BasePresenter<ScanQRContract.View>(), ScanQRContract.Presenter {

    override fun onViewCreated() {
        showScanner()
    }

    override fun onResume() {
        view.startCameraPreview()
    }

    override fun onDismissedDialog() {
        view.startCameraPreview()
    }

    override fun onPause() {
        view.releaseCamera()
    }

    override fun onQRScanned(qr: String) {
        view.sendQRCode(qr)
    }

    override fun onScanError() {
        view.showScanErrorDialog()
    }

    override fun unsubscribe() {

    }

    private fun showScanner() {
        view.apply {
            configureCamera()
            startCameraPreview()
        }
    }
}
