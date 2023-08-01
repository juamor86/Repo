package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.presentation.dialog.CustomLayoutBottomSheetDialog
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import kotlinx.android.synthetic.main.fragment_login_qr.*
import javax.inject.Inject

class LoginQRFragment : BaseFragment(), LoginQRContract.View {

    private val COVID_QR_HELP_DIALOG = "QR_HELP_DIALOG"

    var authEntity: AuthorizeEntity? = null

    //region VARIABLES
    @Inject
    lateinit var presenter: LoginQRContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    //region INITIALIZATION
    override fun bindLayout() = R.layout.fragment_login_qr

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
        requireArguments().getParcelable<AuthorizeEntity>(ApiConstants.Arguments.ARG_AUTHORIZE)?.let { authorizeEntity ->
                authEntity = authorizeEntity
                presenter.onViewCreated(authorizeEntity)
            }
        cleanForm()
        fillQRCode()
    }

    override fun setupView() {
        setupButtons()
        setupIdentifier()
    }

    private fun setupButtons() {
        qr_help_iv.setOnClickListener { presenter.onQRHelpClicked() }
        qr_button_iv.setOnClickListener { presenter.onQRButtonClicked() }
        login_bt.setOnClickListener {
            presenter.onLoginButtonClicked(
                qrCode = qr_code_et.text.toString(),
                idType = identifier_type_tv.currentItem!!.key,
                id = identifier_et.text.toString()
            )
        }
    }

    private fun setupIdentifier() {
        activity?.let { act ->
            identifier_type_tv.setAdapter(
                ArrayAdapter(
                    act,
                    android.R.layout.simple_spinner_dropdown_item,
                    MsspaAuthConsts.documentTypes
                )
            )
        }

        identifier_type_tv.setItem(0)

        identifier_it.apply {
            isHelperTextEnabled = false
            editText?.apply {
                filters =
                    arrayOf(InputFilter.LengthFilter(MsspaAuthConsts.MAX_IDENTIFIER_LENGTH))
                inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                doAfterTextChanged {
                    identifier_type_tv.currentItem?.apply {
                        presenter.checkIdentifier(
                            key,
                            identifier_it.editText?.text.toString()
                        )
                    }
                }
                setOnEditorActionListener { textView, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        presenter.onLoginButtonClicked(
                            qrCode = qr_code_et.text.toString(),
                            idType = identifier_type_tv.currentItem!!.key,
                            id = textView.text.toString()
                        )
                        true
                    } else {
                        false
                    }

                }
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        presenter.validateIdentifier(
                            identifier_type_tv.currentItem!!.key,
                            text.toString()
                        )
                    }
                }
                identifier_type_tv.setOnItemClickListener { _, _, _, _ ->
                    presenter.onIdentifierTypeSelected(
                        identifier_type_tv.currentItem!!.key,
                        identifier_it.editText?.text.toString()
                    )
                }
            }
        }
        identifier_type_tv.setOnClickListener { identifier_type_tv.showDropDown() }
    }

    override fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            launchQRReader()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                MsspaAuthConsts.CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == MsspaAuthConsts.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchQRReader()
            }
        }
    }

    override fun showQRHelp() {
        val environment = (requireActivity() as BaseActivity).authConfig.environment
        val dialog = CustomLayoutBottomSheetDialog(R.layout.view_qr_help, environment)
        activity?.supportFragmentManager?.apply {
            dialog.show(this, COVID_QR_HELP_DIALOG)
        }
    }

    override fun launchQRReader() {
        val bundle = bundleOf(ApiConstants.Arguments.ARG_AUTHORIZE to authEntity)
        findNavController().navigate(R.id.scan_qr_dest, bundle)
    }

    override fun navigateToPin(authEntity: MsspaAuthenticationEntity) {
        val bundle = bundleOf(
            ApiConstants.Arguments.ARG_AUTHENTITY to authEntity
        )
        findNavController().navigate(R.id.qr_login_to_pin_dest, bundle)
    }

    override fun showErrorDNIIdentifier() {
        identifier_it.error = resources.getString(R.string.msspa_auth_dni_error)
    }

    override fun showErrorIdentifier() {
        identifier_it.error = resources.getString(R.string.msspa_auth_identifier_error)
    }

    override fun showErrorNIEIdentifier() {
        identifier_it.error = resources.getString(R.string.msspa_auth_nie_error)
    }

    override fun showErrorNuhsa() {
    }

    override fun showValidIdentifier() {
        identifier_it.error = null
    }

    override fun showValidNuhsa() {
        identifier_it.error = null
    }

    private fun fillQRCode() {
        val qrCode = arguments?.get(ApiConstants.Arguments.ARG_QR_CODE) as String?
        qrCode?.let {
            qr_code_et.setText(it)
        }
    }

    private fun cleanForm() {
        val cleanForm = arguments?.get(ApiConstants.Arguments.ARG_QR_CLEAN_FORM) as Boolean?
        cleanForm?.let {
            qr_code_et.setText("")
            identifier_it.editText?.apply {
                setText("")
            }
        }
    }
}