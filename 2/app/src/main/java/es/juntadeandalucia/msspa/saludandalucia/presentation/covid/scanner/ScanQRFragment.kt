package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomScrollableDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert.CertificateOnBoardingDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert.InvalidCertificateDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert.ValidCertificateDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_scan_qr.scanner_view

class ScanQRFragment : BaseFragment(), ScanQRContract.View {

    companion object {
        private const val COVID_CERT_DIALOG = "cert_dialog"
        internal const val COVID_CERT_ONBOARDING_DIALOG = "cert_onboarding_dialog"
    }

    private var codeScanner: CodeScanner? = null

    @Inject
    lateinit var presenter: ScanQRContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_scan_qr

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

    override fun configureCamera() {
        codeScanner = CodeScanner(requireActivity(), scanner_view).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            // Callbacks
            decodeCallback = DecodeCallback {
                presenter.onQRScanned(it.text)
            }
            errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                presenter.onScanError()
            }
        }
        scanner_view.setOnClickListener {
            codeScanner?.let { it.startPreview()}
        }
    }

    override fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA), Consts.CAMERA_PERMISSION_REQUEST_CODE
                )
            }
            presenter.onCameraPermissionGranted()
        } else {
            presenter.onCameraPermissionGranted()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun startCameraPreview() {
        requireActivity().runOnUiThread {
            codeScanner?.let {it.startPreview()}
        }
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun releaseCamera() {
        codeScanner?.let {it.releaseResources()}
    }

    override fun showPermissionsNeededDialog() {
        showErrorDialog(
            message = R.string.scan_qr_mandatory_permission_dialog_message,
            onAccept = {
                presenter.onAcceptPermissionsNeededDialog()
            })
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    override fun showScanErrorDialog() {
        requireActivity().runOnUiThread {
            showErrorDialog(
                message = R.string.scan_qr_error_dialog_message,
                onAccept = {
                    presenter.onDismissedDialog()
                }
            )
        }
    }

    override fun showValidCovidCert(user: UECovidCertEntity) {
        requireActivity().runOnUiThread {
            val dialog = ValidCertificateDialog(
                user,
                onDismiss = {
                    presenter.onDismissedDialog()
                }
            )
            activity?.supportFragmentManager?.apply {
                dialog.show(this, COVID_CERT_DIALOG)
            }
        }
    }

    override fun showInvalidCovidCert() {
        requireActivity().runOnUiThread {
            val dialog = InvalidCertificateDialog(
                onDismiss = {
                    presenter.onDismissedDialog()
                }
            )
            activity?.supportFragmentManager?.apply {
                dialog.show(this, COVID_CERT_DIALOG)
            }
        }
    }

    override fun showExpiredCovidCert(expirationTime: String) {
        requireActivity().runOnUiThread {
            val dialog = InvalidCertificateDialog(
                onDismiss = {
                    presenter.onDismissedDialog()
                }
            )
            activity?.supportFragmentManager?.apply {
                dialog.show(this, COVID_CERT_DIALOG)
            }
        }
    }

    override fun showScanOnboarding() {
        val dialog = CertificateOnBoardingDialog(
            R.layout.view_item_scan_certificate_on_boarding,
            onDismiss = {
                presenter.onboardingDismissed()
            }
        )
        activity?.supportFragmentManager?.apply {
            dialog.show(this, COVID_CERT_ONBOARDING_DIALOG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.scan_qr_dest)?.isVisible = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == Consts.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onCameraPermissionGranted()
            } else {
                presenter.onCameraPermissionNotGranted()
            }
        }
    }


    override fun showScannerInfoDialog() {
        context?.let {
            CustomScrollableDialog(
                it,
                onAccept = { presenter.infoDialogDismissed() }).show()
        }
    }

    override fun checkCameraPermission(): Boolean {
        val requiredPermission = Manifest.permission.CAMERA
        val checkVal = requireContext().checkCallingOrSelfPermission(requiredPermission)
        return checkVal == PackageManager.PERMISSION_GRANTED
    }
}
