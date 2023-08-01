package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.content.FileProvider
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedFragment
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.BiometricUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_certificate_detail.*
import timber.log.Timber
import java.io.File
import javax.crypto.Cipher
import javax.inject.Inject

class CertificateDetailFragment : LoggedFragment(), CertificateDetailContract.View {

    @Inject
    lateinit var presenter: CertificateDetailContract.Presenter
    private var anyDisposable: Disposable? = null
    private lateinit var certTitle: String
    override fun bindPresenter(): CertificateDetailContract.Presenter = presenter

    @set:Inject
    var cryptoManager: CrytographyManager? = null

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_certificate_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val certType = arguments?.getString(Consts.BUNDLE_CERT_TYPE)!!
        val beneficiary =
            arguments?.getParcelable<BeneficiaryEntity?>(Consts.BUNDLE_CERT_BENEFICIARY)
        certTitle = arguments?.getString(Consts.BUNDLE_CERT_TITLE)!!
        presenter.onViewCreated(certType, beneficiary)
    }

    override fun setupView() {
        animateView()
        download_cert_btn.setOnClickListener { presenter.onDownloadCertClicked() }
        save_cert_btn.setOnClickListener {
            showConfirmDialog(
                R.string.save_covid_certificate_dialog_description,
                onAccept = {
                    presenter.requestBiometrics()
                },
                onCancel = {})
        }
    }

    override fun showSaveCertificate() {
        showConfirmDialog(
            R.string.replace_covid_certificate_dialog_description,
            onAccept = {
                presenter.saveCovidCertificate()
            },
            onCancel = {})
    }

    override fun animateView() {
        main_sv.visibility = View.VISIBLE
        animateViews(main_sv, animId = R.anim.nav_default_enter_anim)
    }

    private fun setCommonData(cert: GreenPassCertEntity) {
        with(cert) {
            covid_cert_title_tv.text = certTitle
            qr_cert_iv.setImageBitmap(qrBitmap)
            name_value_tv.text = userName
            surname_value_tv.text = userSurname
        }
    }

    override fun showCert(cert: GreenPassCertEntity) {
        with(cert) {
            qr_cert_iv.setImageBitmap(qrBitmap)
            setCommonData(this)
        }
    }

    override fun showPDF(pdf: String, pdfNameRes: Int) {
        anyDisposable = Single.fromCallable {
            Utils.getPdf(requireContext(), pdf, getString(pdfNameRes))
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: File? ->
                if (result != null) {
                    presenter.saveFileTempToDelete(file = result)
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

    override fun showPDFErrorDialog() {
        showErrorDialog(R.string.download_pdf_error)
    }

    override fun noCertErrorText(certType: String) {
        showTextError()
        when (certType) {
            Consts.TYPE_VACCINATION -> empty_tv.text = resources.getText(R.string.no_vacinnate_cert)
            Consts.TYPE_TEST -> empty_tv.text = resources.getText(R.string.no_test_cert)
            Consts.TYPE_RECOVERY -> empty_tv.text = resources.getText(R.string.no_recovery_cert)
        }
        empty_tv.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun underAgeCertError() {
        showTextError()
        empty_tv.text = resources.getText(R.string.greenpass_underage_error)
    }

    override fun genericCertError() {
        showTextError()
        empty_tv.text = resources.getText(R.string.greenpass_generic_error_text)
    }

    private fun showTextError() {
        cert_fields_gr.visibility = View.GONE
        empty_view_gr.visibility = View.VISIBLE
    }

    override fun authenticationForEncryption(
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
        showSuccessDialog(R.string.covid_certificate_confirm_dialog_title,onAccept = {})
    }

    override fun showErrorFailedToSaveCert() {
        showErrorDialog(
            title = R.string.error_save_cert
        )
    }

    override fun hideSaveCertificate() {
        save_cert_btn.visibility=View.INVISIBLE
    }

    override fun showDialogNotPhoneSecured() {
        showWarningDialog(title = R.string.dialog_not_phone_secured, onAccept = {})
    }

    override fun haveBiometricOrPin(): Boolean =
        BiometricUtils.isBiometricOrSecured(requireContext())
}
