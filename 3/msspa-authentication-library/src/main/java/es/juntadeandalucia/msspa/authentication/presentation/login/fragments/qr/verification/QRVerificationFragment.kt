package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_qr_verification.*


import javax.crypto.Cipher
import javax.inject.Inject

class QRVerificationFragment : BaseFragment(), QRVerificationContract.View {

    @Inject
    lateinit var presenter: QRVerificationContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_qr_verification

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authEntity =
            requireActivity().intent.extras?.get(MsspaAuthConsts.AUTH_ARG) as MsspaAuthenticationEntity
        val authConfig =
            requireActivity().intent.extras?.get(MsspaAuthConsts.AUTH_CONFIG_ARG) as MsspaAuthenticationConfig
        presenter.onCreate(authEntity, authConfig)
    }

    override fun setupView() {

        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    presenter.cleanLoginQRandFinish()
                }
            })

        can_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                can_it.error = null
                continue_bt.isEnabled = it?.length == MsspaAuthConsts.PIN_LENGHT
            })


        }

        continue_bt.setOnClickListener {
            presenter.onSendPinClicked(
                can_it.editText!!.text.toString(),
                cryptoManager!!
            )
        }

        cancel_btn.setOnClickListener { presenter.onCancelClicked() }
    }

    override fun authenticateForEncryption(
        onSuccess: (Cipher?, Cipher?) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        title: Int?,
        subtitle: Int?,
        negativeButtonText: Int?,
        encrypt: Boolean,
        keyString: String,
        isNeedCipher: Boolean
    ) {
        cryptoManager?.promptForLogin(
            onSuccess = onSuccess,
            onError = onError,
            onErrorInt = onErrorInt,
            keyString = keyString,
            frag = this,
            title = R.string.msspa_auth_pin_prompt_save_title,
            negativeButtonText = R.string.pin_unlock,
            encrypt = true,
            isNeedCipher = isNeedCipher
        )
    }

    override fun showCancelLoginPopUp() {
        showConfirm(
            title = R.string.cancel_login,
            onAccept = { presenter.cleanLoginQRandFinish() },
            onCancel = {}
        )
    }

    override fun showPinError() {
        can_it.error = getText(R.string.pin_incorrect)
    }
}