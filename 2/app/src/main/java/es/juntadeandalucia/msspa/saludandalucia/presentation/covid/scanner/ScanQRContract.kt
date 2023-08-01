package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.scanner

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class ScanQRContract {

    interface View : BaseContract.View {
        fun configureCamera()
        fun requestCameraPermission()
        fun startCameraPreview()
        fun releaseCamera()
        fun showPermissionsNeededDialog()
        fun closeView()
        fun showScanErrorDialog()
        fun showValidCovidCert(user: UECovidCertEntity)
        fun showInvalidCovidCert()
        fun showScanOnboarding()
        fun showExpiredCovidCert(expirationTime: String)
        fun showScannerInfoDialog()
        fun checkCameraPermission():Boolean
    }

    interface Presenter : BaseContract.Presenter {
        fun onResume()
        fun onPause()
        fun onCameraPermissionGranted()
        fun onCameraPermissionNotGranted()
        fun onAcceptPermissionsNeededDialog()
        fun onQRScanned(qr: String)
        fun onScanError()
        fun onDismissedDialog()
        fun onboardingDismissed()
        fun infoDialogDismissed()
    }
}
