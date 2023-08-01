package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.secondfactor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts.Companion.REFRESH_OTP_TIME_MILLIS
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants.Arguments.ARG_AUTHORIZE
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants.Arguments.ARG_QUIZ_AUTHORIZE
import kotlinx.android.synthetic.main.fragment_second_factor.*
import timber.log.Timber
import javax.inject.Inject

class SecondFactorFragment : BaseFragment(), SecondFactorContract.View {
    private lateinit var receiver: BroadcastReceiver

    //region VARIABLES
    @Inject
    lateinit var presenter: SecondFactorContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    private var timer: CountDownTimer? = null

    private var authorizeEntity: AuthorizeEntity? = null
    private var msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity? = null
    //endregion

    //region INITIALIZATION
    override fun bindLayout() = R.layout.fragment_second_factor

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val totpCode = intent?.extras?.get(MsspaAuthConsts.TOTP_CODE)
                fillTotp(totpCode as String?)
            }
        }

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(MsspaAuthConsts.TOTP_ACTION))

    }

    private fun fillTotp(totpCode: String?) {
        totpCode?.let {
            if(code_et?.isVisible == true){
                code_et?.setText(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(requireArguments()) {
            getParcelable<AuthorizeEntity>(ARG_AUTHORIZE)?.let { authorizedUser ->
                authorizeEntity = authorizedUser

                getParcelable<MsspaAuthenticationUserEntity>(ARG_QUIZ_AUTHORIZE)?.let { quizUser ->
                    msspaAuthenticationUserEntity = quizUser

                    presenter.onViewCreated(
                        (activity as BaseActivity).authConfig,
                        authorizedUser,
                        quizUser,
                        getBoolean(ApiConstants.Arguments.ARG_SAVE_USER)
                    )
                }
            }
        }
    }

    override fun enableAutoComplete(active: Boolean) {
        // TODO: jmtm - Test purpose only. Remove it
//        if (active) {
//            sasIncomingSmsReceiver = SasIncomingSmsReceiver()
//            SasIncomingSmsReceiver.otpReceiver = this
//            SasIncomingSmsReceiver.fragment = this
//            sasIncomingSmsReceiver?.register()
//        } else {
//            sasIncomingSmsReceiver?.unregister()
//        }
    }

    override fun setupView() {
        initCodeInput()
        countdown_pb?.max = (REFRESH_OTP_TIME_MILLIS / 1000).toInt()
        continue_bt?.setOnClickListener { presenter.onValidateClicked(code_it.editText?.text.toString()) }
        refresh_bt?.setOnClickListener { presenter.onRefreshClick() }
        autentication_method_cw?.setOnClickListener {
            presenter.onOtherAuthMethodClicked()
        }
    }

    private fun initCodeInput() {
        code_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.onCodeTextChanged(it.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    presenter.onValidateClicked(code_it.editText?.text.toString())
                    true
                }
                false
            }
        }
    }
    //endregion

    //region EVENTS & METHODS

    override fun showTotpCountdown() {
        countdown_pb?.visibility = View.VISIBLE
        countdown_tv?.visibility = View.VISIBLE
    }

    override fun showRefreshButton() {
        refresh_bt?.visibility = View.VISIBLE
    }

    override fun hideTotpCountdown() {
        countdown_pb?.visibility = View.INVISIBLE
        countdown_tv?.visibility = View.INVISIBLE
    }

    override fun hideRefreshButton() {
        refresh_bt?.visibility = View.INVISIBLE
    }

    override fun stopTotpCountDown() {
        timer?.cancel()
    }

    override fun startTotpCountDown(expirationMillis: Long) {
        stopTotpCountDown()

        timer = object : CountDownTimer(expirationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secs: Int = (millisUntilFinished / 1000).toInt()
                countdown_pb?.progress = secs
                countdown_tv?.text = secs.toString()
            }

            override fun onFinish() {
                presenter.onCountdownFinish()
            }
        }.start()
    }

    override fun enableContinueButton(active: Boolean) {
        continue_bt?.isEnabled = active
    }

    override fun showWarning(
        warning: MsspaAuthenticationWarning,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        super.showWarning(
            warning,
            onAccept = {
                findNavController().popBackStack(R.id.auth_webview_dest, false)
            },
            onCancel = onCancel
        )
    }


    override fun onDestroy() {
        stopTotpCountDown()
        try {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        } catch (e: Exception) {
            Timber.e(e)
        }
        super.onDestroy()
    }
    //endregion
}