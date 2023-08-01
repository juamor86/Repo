package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.phonevalidation

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.BuildConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.Utils
import kotlinx.android.synthetic.main.fragment_phone_validation.*
import javax.inject.Inject

class PhoneValidationFragment : BaseFragment(), PhoneValidationContract.View {
    //region VARIABLES
    @Inject
    lateinit var presenter: PhoneValidationContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter
    //endregion

    //region INITIALIZATION
    override fun bindLayout() = R.layout.fragment_phone_validation

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
        with(requireArguments()) {
            getParcelable<MsspaAuthenticationEntity>(MsspaAuthConsts.AUTH_ARG)?.let { authEntity ->
                getParcelable<AuthorizeEntity>(ApiConstants.Arguments.ARG_AUTHORIZE)?.let { authorizeEntity ->
                    presenter.onViewCreated(authEntity, authorizeEntity)
                }
            }
        }

        if (BuildConfig.DEBUG) {
            pin_et.setText("654266544")
        }
    }

    override fun setupView() {
        initExplanationTextView()
        initPhoneInput()
        initContinueButton()
        initAuthenticationCard()
    }

    private fun initExplanationTextView() {
        explanation_tv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initContinueButton() {
        continue_bt.setOnClickListener {
            disableLoginButton()
            presenter.onContinueClick(can_it.editText?.text.toString())
        }
    }

    private fun initAuthenticationCard() {
        autentication_method_cw.setOnClickListener {
            presenter.onOtherAuthMethodClicked()
        }
    }


    private fun initPhoneInput() {
        can_it.editText?.apply {
            filters = arrayOf(Utils.whiteSpaceFilter)
            addTextChangedListener(afterTextChanged = {
                presenter.checkPhoneNumber(
                        text.toString()
                )
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validatePhoneNumber(
                            text.toString()
                    )

                    hideKeyboard()
                }
            }
        }
    }
    //endregion

    //region EVENTS & METHODS
    override fun navigateToSMS(authorizeEntity: AuthorizeEntity, msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity) {
        val bundle = bundleOf(ApiConstants.Arguments.ARG_AUTHORIZE to authorizeEntity,
                ApiConstants.Arguments.ARG_QUIZ_AUTHORIZE to msspaAuthenticationUserEntity,
                ApiConstants.Arguments.ARG_SAVE_USER to false)
        findNavController().navigate(R.id.phone_validation_dest_to_second_factor_dest, bundle)
    }


    override fun showValidPhoneNumber() {
        can_it.isErrorEnabled = false
    }

    override fun enableLoginButton() {
        continue_bt.isEnabled = true
    }

    override fun disableLoginButton() {
        continue_bt.isEnabled = false
    }


    override fun showErrorPhoneNumber() {
        can_it.error = getString(R.string.msspa_auth_error_phone)
    }

    override fun showWarning(warning: MsspaAuthenticationWarning, onAccept: (() -> Unit)?, onCancel: (() -> Unit)?) {
        super.showWarning(
                warning,
                onAccept = {
                    findNavController().popBackStack(R.id.auth_webview_dest, false)
                },
                onCancel = onCancel
        )
    }
    //endregion
}