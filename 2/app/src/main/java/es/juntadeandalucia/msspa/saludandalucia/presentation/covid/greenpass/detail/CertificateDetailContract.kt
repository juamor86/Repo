package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedContract
import java.io.File
import javax.crypto.Cipher

class CertificateDetailContract {

    interface View : LoggedContract.View {
        fun setupView()
        fun animateView()
        fun showCert(cert: GreenPassCertEntity)
        fun showPDF(pdf: String, pdfNameRes: Int)
        fun showPDFErrorDialog()
        fun noCertErrorText(certType: String)
        fun underAgeCertError()
        fun genericCertError()
        fun authenticationForEncryption(
            onSuccess: (Cipher, Cipher) -> Unit,
            onError: (String) -> Unit,
            unlockEntity: UnlockEntity?
        )
        fun showSaveCertificate()
        fun showCertificateSavedDialog()
        fun showErrorFailedToSaveCert()
        fun hideSaveCertificate()
        fun haveBiometricOrPin():Boolean
        fun showDialogNotPhoneSecured()
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(
            certType: String,
            beneficiary: BeneficiaryEntity?
        )
        fun checkForCertificateSaved()
        fun onDownloadCertClicked()
        fun requestBiometrics()
        fun saveCovidCertificate()
        fun saveFileTempToDelete(file: File?)
    }
}
