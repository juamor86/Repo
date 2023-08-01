package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.certificate

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UserCovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedFragment
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_covid_cert.*
import timber.log.Timber
import java.io.File
import javax.crypto.Cipher
import javax.inject.Inject

class CovidCertFragment : LoggedFragment(), CovidCertContract.View {

    private var refreshQRButton: MenuItem? = null
    private var anyDisposable: Disposable? = null

    @Inject
    lateinit var presenter: CovidCertContract.Presenter

    @set:Inject
    var cryptoManager: CrytographyManager? = null

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_covid_cert

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        download_cert_btn.setOnClickListener { presenter.onDownloadCertClicked() }
        val beneficiary =
            arguments?.getParcelable<BeneficiaryEntity?>(Consts.BUNDLE_CERT_BENEFICIARY)
        presenter.onViewCreated(beneficiary)
        save_cert_btn.setOnClickListener {
            showConfirmDialog(
                R.string.save_covid_certificate_dialog_description,
                onAccept = {
                    presenter.requestBiometrics()
                },
                onCancel = {})
        }
        presenter.onViewCreated()
    }

    override fun showVaccineData(
        covidCertEntity: UserCovidCertEntity,
        idType: String,
        idValue: String
    ) {
        with(covidCertEntity) {
            name_value_tv.text = userName
            last_name_value_tv.text = userLastName
            id_type_value_tv.text = idType
            document_value_tv.text = idValue
            qr_cert_iv.setImageBitmap(qrBitmap)
        }
    }

    override fun hideAll() {
        main_ly?.visibility = View.GONE
    }

    override fun showAll() {
        main_ly?.visibility = View.VISIBLE
    }

    override fun hideQR() {
        qr_cert_iv?.visibility = View.INVISIBLE
    }

    override fun showPDF(pdf: String) {
        anyDisposable = Single.fromCallable {
            Utils.getPdf(
                requireContext(),
                pdf,
                getString(R.string.contraindications_covid_pdf_name)
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: File? ->
                if (result != null) {
                    presenter.saveFileTempToDelete(result)
                    val path: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireActivity().applicationContext.packageName + ".provider",
                        result
                    )
                    val target = Intent(Intent.ACTION_VIEW)
                    target.setDataAndType(path, "application/pdf")
                    target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    val intent = Intent.createChooser(target, getString(R.string.open_covid_cert))
                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        showPDFErrorDialog()
                    }
                } else {
                    showPDFErrorDialog()
                }
            }
    }

    override fun navigateBack() {
        findNavController().navigateUp()
    }

    override fun showNotVaccine() {
        vaccine_gr.visibility = View.GONE
        empty_view_gr.visibility = View.VISIBLE
        refreshQRButton?.isVisible = false
    }

    override fun showNotLoggedError() {
        showErrorDialog(R.string.user_not_logged)
    }

    override fun showPDFErrorDialog() {
        showErrorDialog(R.string.download_pdf_error)
    }

    override fun promptForEncryption(
        onSuccess: (Cipher, Cipher) -> Unit,
        onError: (String) -> Unit,
        unlockEntity: UnlockEntity?
    ) {
        cryptoManager?.promptForEncryption(
            onSuccess,
            onError,
            requireActivity(),
            Consts.IDENTIFIER,
            unlockEntity
        )
    }

    override fun showCertificateSavedDialog() {
        showSuccessDialog(R.string.covid_certificate_confirm_dialog_title, onAccept = {})
    }

    override fun onDestroy() {
        super.onDestroy()
        anyDisposable?.dispose()
    }

    override fun showErrorFailedToSaveCert() {
        showErrorDialog(
            title = R.string.error_save_cert
        )
    }

    override fun testSave(totest: UserCovidCertEntity) {
        totest.jwt?.let {
            //PinPatternCryptographyManager().encryptAndSavePassword(it)
        }
    }

    override fun hideSaveCertificate() {
        save_cert_btn.visibility = View.INVISIBLE
    }

    override fun showSaveCertificate() {
        showConfirmDialog(
            R.string.replace_covid_certificate_dialog_description,
            onAccept = {
                presenter.saveCovidCertificate()
            },
            onCancel = {})
    }
}
