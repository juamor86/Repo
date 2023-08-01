package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.secondfactor

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.crypto.Cipher
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_covid_second_factor.*

class SecondFactorFragment : BaseFragment(), SecondFactorContract.View {

    @Inject
    lateinit var presenter: SecondFactorContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    @set:Inject
    var cryptoManager: CrytographyManager? = null

    override fun bindLayout() = R.layout.fragment_covid_second_factor

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(requireArguments()) {
            presenter.apply {
                onViewCreated()
                setupView(
                    getParcelable(Consts.ARG_AUTHORIZE)!!,
                    getParcelable(Consts.ARG_USER)!!,
                    getBoolean(Consts.ARG_SAVE_USER)
                )
            }
        }
    }

    override fun setupView() {
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
                    presenter.onContinueClick(code_it.editText?.text.toString())
                    true
                }
                false
            }
        }
        continue_bt.setOnClickListener { presenter.onContinueClick(code_it.editText?.text.toString()) }
    }

    override fun navigateToQuiz(user: QuizUserEntity) {
        val bundle =
            bundleOf(Consts.ARG_USER to user)
        findNavController().navigate(R.id.second_factor_to_quiz_dest, bundle)
    }

    override fun showLoginDefaultError() {
        showErrorDialog(R.string.error_login_default)
    }

    override fun showInvalidCodeError() {
        showErrorDialog(R.string.login_code_error)
    }

    override fun authenticateForEncryption(
        onSuccess: (Cipher, Cipher) -> Unit,
        onError: (String) -> Unit
    ) {
        cryptoManager?.promptForEncryption(
            onSuccess, onError, requireActivity(),
            Consts.KEY_SAVED_USERS
        )
    }

    override fun enableContinueButton(enable: Boolean) {
        continue_bt.isEnabled = enable
    }

    override fun fillCodeAndContinue(code: String) {
        code_it.editText?.setText(code)
        continue_bt.performClick()
    }
}
