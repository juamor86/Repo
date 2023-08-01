package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.certificate

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.NoCertificateException
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UserCovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.CertificateMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import retrofit2.HttpException
import timber.log.Timber
import java.io.File
import java.net.HttpURLConnection

class CovidCertPresenter(
    private val getUserCovidCertDataUseCase: GetUserCovidCertDataUseCase,
    private val downloadCertificatePDFUseCase: DownloadUserCovidCertPDFUseCase,
    private val checkCovidCertificateUseCase: CheckCovidCertificateUseCase,
    private val saveCovidCertificateUseCase: SaveCovidCertificateUseCase,
    private val getWalletIsActiveUseCase: GetWalletIsActiveUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val sessionBus: SessionBus
) :
    BasePresenter<CovidCertContract.View>(), CovidCertContract.Presenter {

    // TODO: 27/12/2021 Duplicate code, unify with green pass cert

    private var accessToken: String? = null
    private var certificateContent: UserCovidCertEntity? = null
    private var nuhsa: String? = null
    private var isCertSaved = false
    private var idType: String = ""
    private var idValue: String = ""
    private var fileTempToDelete: File? = null

    override fun onViewCreated(beneficiary: BeneficiaryEntity?) {
        checkWalletIsActivated()
        view.hideAll()
        getUserCovidCert()
        accessToken = beneficiary?.token
        nuhsa = beneficiary?.nuhsa
    }

    private fun checkWalletIsActivated() {
        val isWalletActivated = getWalletIsActiveUseCase.execute()
        if (!isWalletActivated) {
            view.hideSaveCertificate()
        }
    }

    override fun getScreenNameTracking(): String? =
        Consts.Analytics.COVID_CERTIFICATE_ADL_SCREEN_ACCESS

    private fun getUserCovidCert() {
        view.showLoading()
        getUserCovidCertDataUseCase.params(accessToken).execute(onSuccess = { covidEntity->
            certificateContent = covidEntity
            view.apply {
                hideLoading()
                showAll()

                if (covidEntity.hasVaccine) {
                    getDocumentData(covidEntity.jwt)
                    showVaccineData(covidEntity, idType, idValue)
                } else {
                    showNotVaccine()
                }
            }
        }, onError = {
            Timber.e(it)
            view.hideLoading()
            when (it) {
                is NoCertificateException -> {
                    view.apply {
                        showAll()
                        showNotVaccine()
                    }
                }
                is HttpException -> {
                    when (it.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> {
                            view.apply {
                                showAll()
                                showNotVaccine()
                            }
                        }
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            view.apply {
                                informUserNotLogged()
                                navigateBack()
                            }
                        }
                    }
                }
                else -> {
                    view.apply {
                        showErrorDialog()
                        navigateBack()
                    }
                }
            }
        })
    }

    private fun getDocumentData(token: String?) {
        token?.let { token ->
            val json = String(Base64.decode(token.split(".")[1], Base64.DEFAULT))
            val jsonObj = Gson().fromJson(json, JsonObject::class.java)
            idType = jsonObj.get(Consts.ID_TYPE_PARAMETER).asString
            idValue = jsonObj.get(Consts.ID_VALUE_PARAMETER).asString
        }

    }

    override fun onDownloadCertClicked() {
        view.showLoadingBlocking()
        downloadCertificatePDFUseCase.params(accessToken).execute(onSuccess = {
            view.hideLoading()
            it?.let { view.showPDF(it) } ?: view.showPDFErrorDialog()
        }, onError = {
            Timber.e(it)
            view.apply {
                hideLoading()
                hideAll()
                showPDFErrorDialog()
            }
        })
    }

    override fun checkForCertificateSaved() {

        val id = (nuhsa
            ?: sessionBus.session.msspaAuthenticationEntity.msspaAuthenticationUser?.nuhsa!!) + Utils.getCovidCertificateCode(
            ""
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
                    saveCovidCertificate()
                }
            )

    }

    override fun requestBiometrics() {
        view.promptForEncryption(
            onSuccess = { _, _ ->
                checkForCertificateSaved()
            },
            onError = {
                Timber.e("Error at unlock")
            },
            UnlockEntity.SAVE_CERT

        )
    }

    override fun saveCovidCertificate() {
        val userId =
            nuhsa ?: sessionBus.session.msspaAuthenticationEntity.msspaAuthenticationUser?.nuhsa

        certificateContent?.apply {
            saveCovidCertificateUseCase.params(
                CertificateMapper.convert(this, userId!!),
                isCertSaved
            ).execute(onComplete = {
                view.showCertificateSavedDialog()
                view.sendEvent(Consts.Analytics.SAVE_CERT_WALLET + Consts.TYPE_CONTRAINDICATIONS)
            }, onError = {
                view.showErrorFailedToSaveCert()
            })
        }
    }

    override fun saveFileTempToDelete(file: File?) {
        fileTempToDelete = file
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
        getUserCovidCertDataUseCase.clear()
        downloadCertificatePDFUseCase.clear()
    }
}
