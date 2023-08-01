package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.scanner

import ehn.techiop.hcert.kotlin.chain.Error
import ehn.techiop.hcert.kotlin.chain.VerificationException
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ValidateContraindicationUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ValidateGreenPassUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class ScanQRPresenter(
    private val gitFirstAccess: GetFirstAccessUseCase,
    private val saveFirstAccess: SetFirstAccessUseCase,
    private val validateGreenPassUseCase: ValidateGreenPassUseCase,
    private val validateContraindicationUseCase: ValidateContraindicationUseCase
) :
    BasePresenter<ScanQRContract.View>(), ScanQRContract.Presenter {

    var cameraPermissionGranted = false

    override fun getScreenNameTracking(): String? = Consts.Analytics.COVID_CERTIFICATE_QR_VALIDATION

    override fun onViewCreated() {
        if (gitFirstAccess.param(Consts.PREF_FIRST_ACCESS_TO_SCAN_CERTIFICATE).execute()) {
            view.showScanOnboarding()
            saveFirstAccess
                .param(Consts.PREF_FIRST_ACCESS_TO_SCAN_CERTIFICATE)
                .execute(
                    onComplete = {},
                    onError = { Timber.e(it) }
                )
        } else {
            view.showScannerInfoDialog()
        }
    }

    override fun onCameraPermissionGranted() {
        cameraPermissionGranted = true
    }

    override fun onCameraPermissionNotGranted() {
        cameraPermissionGranted = false
        view.showPermissionsNeededDialog()
    }

    override fun onAcceptPermissionsNeededDialog() {
        view.closeView()
    }

    override fun onResume() {
        if (cameraPermissionGranted) {
            view.startCameraPreview()
        }
    }

    override fun onboardingDismissed() {
        view.showScannerInfoDialog()
    }

    override fun infoDialogDismissed() {
        view.apply {
            configureCamera()
            requestCameraPermission()
            cameraPermissionGranted = checkCameraPermission()
            if (cameraPermissionGranted) {
                startCameraPreview()
            }
        }
    }

    override fun onDismissedDialog() {
        if (cameraPermissionGranted) {
            view.startCameraPreview()
        }
    }

    override fun onPause() {
        view.releaseCamera()
    }

    override fun onQRScanned(qr: String) {
        try {
            validateGreenPassUseCase.params(qr).execute(
                onSuccess = { user ->
                    if (user.isOk) {
                        view.showValidCovidCert(user)
                    } else {
                        validateContraindicationCert(qr)
                    }
                },
                onError = {
                    if (it is VerificationException) {
                        if (it.error == Error.CWT_EXPIRED) {
                            val expirationTime = it.details?.get("expirationTime") ?: ""
                            view.showExpiredCovidCert(expirationTime)
                        } else {
                            view.showInvalidCovidCert()
                        }
                    }else {
                        Timber.e(it, "Error validating certificate")
                        view.showErrorDialog(R.string.error_cert_validate)
                    }
                })
        } catch (ex: Exception) {
            Timber.e(ex, "Error verifying the JWT token")
            validateContraindicationCert(qr)
        }
    }

    override fun onScanError() {
        view.showScanErrorDialog()
    }

    private fun validateContraindicationCert(qr: String) {
        validateContraindicationUseCase.params(qr).execute(
            onSuccess = { user ->
                if (user.isOk) {
                    view.showValidCovidCert(user)
                } else {
                    view.showInvalidCovidCert()
                }
            },
            onError = {
                Timber.i(it)
                view.showInvalidCovidCert()
            }
        )
    }

    override fun unsubscribe() {
        saveFirstAccess.clear()
        validateContraindicationUseCase.clear()
        validateGreenPassUseCase.clear()
    }
}
