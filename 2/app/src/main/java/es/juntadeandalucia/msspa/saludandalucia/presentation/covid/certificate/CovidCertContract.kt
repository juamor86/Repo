package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.certificate

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UserCovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedContract
import java.io.File
import javax.crypto.Cipher

class CovidCertContract {

    interface View : LoggedContract.View {
        fun hideAll()
        fun showVaccineData(covidCertEntity: UserCovidCertEntity, idType: String, idValue: String)
        fun showNotVaccine()
        fun navigateBack()
        fun showNotLoggedError()
        fun hideQR()
        fun showAll()
        fun showPDF(pdf: String)
        fun showPDFErrorDialog()
        fun promptForEncryption(
            onSuccess: (Cipher, Cipher) -> Unit,
            onError: (String) -> Unit,
            unlockEntity: UnlockEntity?
        )

        fun showSaveCertificate()
        fun showCertificateSavedDialog()
        fun showErrorFailedToSaveCert()
        fun testSave(totest: UserCovidCertEntity)
        fun hideSaveCertificate()
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(beneficiary: BeneficiaryEntity?)
        fun onDownloadCertClicked()
        fun checkForCertificateSaved()
        fun requestBiometrics()
        fun saveCovidCertificate()
        fun saveFileTempToDelete(file: File?)
    }
}
