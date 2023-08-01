package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.detail

import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.NoCertificateException
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.Under16LoginException
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.CertificateMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import timber.log.Timber
import java.io.File

class CertificateDetailPresenter(
    private val getUserGreenPassCertUseCase: GetUserGreenPassCertUseCase,
    private val downloadGreenPassPdfUseCase: DownloadGreenPassPdfUseCase,
    private val saveCovidCertificateUseCase: SaveCovidCertificateUseCase,
    private val checkCovidCertificateUseCase: CheckCovidCertificateUseCase,
    private val getWalletIsActiveUseCase: GetWalletIsActiveUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val sessionBus: SessionBus
) : BasePresenter<CertificateDetailContract.View>(),
    CertificateDetailContract.Presenter {

    // TODO: 27/12/2021 Duplicate code, unify with Andalusian cert

    private lateinit var certType: String
    private var accessToken: String? = null
    private var nuhsa: String? = null
    private var certificateContent: GreenPassCertEntity? = null
    private var isCertSaved = false
    private var fileTempToDelete: File? = null

    override fun onViewCreated(
        certType: String,
        beneficiary: BeneficiaryEntity?
    ) {
        checkWalletIsActivated()
        view.apply {
            sendEvent(Consts.Analytics.CERTIFICATE_CERT_ACCESS + certType)
            showLoading()
        }
        this.certType = certType

        // FIXME: 13/07/2021 Change the logic to get the cert, there should be two use cases
        accessToken = beneficiary?.token
        nuhsa = beneficiary?.nuhsa
        getUserGreenPassCertUseCase.params(certType, accessToken)
        getUserGreenPassCertUseCase.execute(
            onSuccess =
            {
                view.sendEvent(Consts.Analytics.CERTIFICATION_VALIDATION_OK)
                certificateContent = it
                checkCert(it)
                view.apply {
                    hideLoading()
                    setupView()
                }
            },
            onError =
            {
                view.apply {
                    sendEvent(Consts.Analytics.CERTIFICATION_VALIDATION_KO)
                    hideLoading()
                    animateView()
                }
                Timber.e(it)
                view.underAgeCertError()
                when (it) {
                    is Under16LoginException -> {
                        view.underAgeCertError()
                    }
                    is NoCertificateException -> {
                        view.noCertErrorText(certType)
                    }
                    else -> view.genericCertError()
                }
            }
        )
    }

    private fun checkWalletIsActivated() {
        if (!getWalletIsActiveUseCase.execute()) {
            view.hideSaveCertificate()
        }
    }

    private fun checkCert(cert: GreenPassCertEntity) {
        view.showCert(cert)
    }

    override fun onDownloadCertClicked() {
        view.showLoading()

        downloadGreenPassPdfUseCase.params(Consts.FORMAT_PDF, certType, accessToken)

        downloadGreenPassPdfUseCase.execute(
            onSuccess = {
                view.apply {
                    sendEvent(Consts.Analytics.CERTIFICATE_DOWNLOAD_OK.replace("[type]", certType))
                    hideLoading()
                }

                it?.let { showPDF(it) } ?: view.showPDFErrorDialog()
            },
            onError = {
                view.apply {
                    sendEvent(Consts.Analytics.CERTIFICATE_DOWNLOAD_KO.replace("[type]", certType))
                    hideLoading()
                    showPDFErrorDialog()
                }
                Timber.e(it)
            }
        )
    }

    override fun checkForCertificateSaved() {

        val id = (nuhsa
            ?: sessionBus.session.msspaAuthenticationEntity.msspaAuthenticationUser?.nuhsa!!) + Utils.getCovidCertificateCode(
            certType
        )
        checkCovidCertificateUseCase.params(PinPatternCryptographyManager.encryptAndSavePassword(id))
            .execute(
                onSuccess = { isSaved ->
                    if (isSaved) {
                        isCertSaved = isSaved
                        view.showSaveCertificate()
                    } else {
                        saveCovidCertificate()
                    }
                },
                onError = {
                    Timber.e(it)
                    saveCovidCertificate()
                }
            )
    }

    override fun requestBiometrics() {
        view.apply {
            if(haveBiometricOrPin()){
                view.authenticationForEncryption(
                    onSuccess = { _, _ ->
                        checkForCertificateSaved()
                    },
                    onError = {
                        Timber.e("Error at unlock")
                    },
                    UnlockEntity.SAVE_CERT
                )
            }else{
                showDialogNotPhoneSecured()
            }
        }
    }

    override fun saveCovidCertificate() {
        val userId =
            nuhsa ?: sessionBus.session.msspaAuthenticationEntity.msspaAuthenticationUser?.nuhsa
        certificateContent?.apply {

            saveCovidCertificateUseCase.params(
                CertificateMapper.convert(
                    covidCertificate = this,
                    certType = certType,
                    userId = userId!!
                ),
                isCertSaved
            ).execute(onComplete = {
                view.showCertificateSavedDialog()
                view.sendEvent(Consts.Analytics.SAVE_CERT_WALLET + certType)
            }, onError = {
                view.showErrorFailedToSaveCert()
            })
        }
    }

    override fun saveFileTempToDelete(file: File?) {
        fileTempToDelete = file
    }

    private fun showPDF(pdf: String) {
        val pdfNameRes = when (certType) {
            Consts.TYPE_VACCINATION -> R.string.vaccine_pdf_name
            Consts.TYPE_TEST -> R.string.test_pdf_name
            Consts.TYPE_RECOVERY -> R.string.recovery_pdf_name
            else -> R.string.vaccine_pdf_name
        }
        view.showPDF(pdf, pdfNameRes)
    }

    private fun deleteFileTemp() {
        fileTempToDelete?.let {
            deleteFileUseCase.param(it).execute(
                onComplete = { Timber.i("Delete file temp successfully") },
                onError = { Timber.e(it) }
            )
        }
    }

    override fun unsubscribe() {
        deleteFileTemp()
        view.hideLoading()
        getUserGreenPassCertUseCase.clear()
        downloadGreenPassPdfUseCase.clear()
    }
}
