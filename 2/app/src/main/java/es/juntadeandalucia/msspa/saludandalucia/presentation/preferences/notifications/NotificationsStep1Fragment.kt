package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import android.Manifest
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_notifications_step_1.*

class NotificationsStep1Fragment : BaseFragment(), NotificationsStep1Contract.View {

    @Inject
    lateinit var presenter: NotificationsStep1Contract.Presenter
    private var haveToNavigateHome: Boolean = false
    private var dest: NavigationEntity? = null

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>


    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_notifications_step_1

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

       /* val permissionsGiven = mutableMapOf(
            Manifest.permission.READ_SMS to true,
            Manifest.permission.RECEIVE_SMS to true
        )
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when (permissions) {
                    permissionsGiven -> {}
                    else -> {}
                }
            }*/
    }

    /*fun requestPermissions() {
        locationPermissionRequest.launch(arrayOf(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS))
    }
*/
    override fun setupView() {
        haveToNavigateHome = arguments?.getBoolean(Consts.ARG_HAVE_TO_NAVIGATE_HOME) == true
        dest = arguments?.getParcelable(Consts.ARG_ADVICES_NAVIGATION_ENTITY)
        //requestPermissions()
        // Phone prefix
        phone_prefix_it.editText?.apply {
            setText(Consts.SPAIN_PREFIX)
            addTextChangedListener(afterTextChanged = {
                ensurePhonePrefixPlus()
                setMaxLengthPhoneInput()
                presenter.checkPhonePrefix(text.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validatePhonePrefix(text.toString())
                    presenter.validatePhoneNumber(
                        text.toString(),
                        phone_it.editText?.text.toString()
                    )
                    hideKeyboard()
                }
            }
            setSelection(text?.length ?: 0)
        }
        // Phone number
        phone_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.checkPhoneNumber(
                    phone_prefix_it.editText?.text.toString(),
                    text.toString()
                )
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validatePhoneNumber(
                        phone_prefix_it.editText?.text.toString(),
                        text.toString()
                    )
                    hideKeyboard()
                }
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    presenter.onNextStepClicked(
                        phone_prefix_it.editText?.text.toString(),
                        phone_it.editText?.text.toString()
                    )
                    true
                }
                false
            }
        }
        // Continue button
        verification_step_1_continue_bt.setOnClickListener {
            presenter.onNextStepClicked(
                phone_prefix_it.editText?.text.toString(),
                phone_it.editText?.text.toString()
            )
        }
    }

    private fun ensurePhonePrefixPlus() {
        phone_prefix_it.editText?.text?.toString()?.apply {
            if (!startsWith(Consts.PLUS)) {
                val cleanString: String
                val deletedPrefix = Consts.PLUS.substring(0, Consts.PLUS.length - 1)
                cleanString = if (startsWith(deletedPrefix)) {
                    replace(deletedPrefix, "")
                } else {
                    replace(Consts.PLUS, "")
                }
                phone_prefix_it.editText?.apply {
                    setText(Consts.PLUS + cleanString)
                    setSelection(Consts.PLUS.length)
                }
            }
        }
    }

    private fun setMaxLengthPhoneInput() {
        phone_et.filters =
            when (phone_prefix_it.editText?.text.toString()) {
                Consts.SPAIN_PREFIX -> arrayOf(InputFilter.LengthFilter(Consts.VALIDATION_MAX_LENGTH_SPANISH_PHONE_NUMBER))
                else -> emptyArray()
            }
    }

    override fun showValidPhoneNumber() {
        phone_it.isErrorEnabled = false
    }

    override fun showErrorPhoneNumber() {
        phone_it.error = getString(R.string.error_phone)
    }

    override fun showValidPhonePrefix() {
        phone_prefix_it.isErrorEnabled = false
    }

    override fun showErrorPhonePrefix() {
        phone_prefix_it.error = getString(R.string.error_prefix)
    }

    override fun showInputErrorsDialog() {
        showErrorDialog(R.string.form_input_errors)
    }

    override fun showMaxAttemptsDialog() {
        showErrorDialog(R.string.error_verification_max_attempts)
    }

    override fun navigateToStep2(verificationCodeEntity: RequestVerificationCodeEntity) {
        val bundle = bundleOf(Consts.ARG_VERIFICATION to verificationCodeEntity)
        if (haveToNavigateHome) {
            bundle.putBoolean(Consts.ARG_HAVE_TO_NAVIGATE_HOME, haveToNavigateHome)
            bundle.putParcelable(Consts.ARG_ADVICES_NAVIGATION_ENTITY, dest)
        }
        findNavController().navigate(
            R.id.action_notifications_step_1_dest_to_notifications_step_2_dest,
            bundle
        )
    }
}
