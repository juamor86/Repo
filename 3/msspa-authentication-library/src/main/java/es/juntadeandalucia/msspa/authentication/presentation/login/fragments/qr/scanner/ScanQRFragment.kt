package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.scanner

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import kotlinx.android.synthetic.main.fragment_scan_qr.*
import javax.inject.Inject

class ScanQRFragment : BaseFragment(), ScanQRContract.View {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navigateUp()
        }
    }

    private var codeScanner: CodeScanner? = null

    var qrCode: String? = null
    var authEntity: AuthorizeEntity? = null

    @Inject
    lateinit var presenter: ScanQRContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_scan_qr

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        requireArguments().getParcelable<AuthorizeEntity>(ApiConstants.Arguments.ARG_AUTHORIZE)?.let { authorizeEntity ->
                authEntity = authorizeEntity
            }
        presenter.onViewCreated()
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
            codeScanner?.let { it.startPreview() }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun startCameraPreview() {
        requireActivity().runOnUiThread {
            codeScanner?.let { it.startPreview() }
        }
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun releaseCamera() {
        codeScanner?.let { it.releaseResources() }
    }

    override fun showScanErrorDialog() {
        requireActivity().runOnUiThread {
            showDialog(
                title = R.string.scan_qr_error_dialog_message,
                onAccept = {},
                onCancel = {}
            )
        }
    }

    override fun sendQRCode(qr: String) {
        qrCode = qr
        requireActivity().runOnUiThread {
            navigateUp()
        }
    }

    private fun navigateUp() {
        val bundle = bundleOf(
            ApiConstants.Arguments.ARG_QR_CODE to qrCode,
            ApiConstants.Arguments.ARG_AUTHORIZE to authEntity
        )
        findNavController().navigate(R.id.action_scan_qr_to_qr_login_dest, bundle)
    }
}
