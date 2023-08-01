package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_notifications_step_2.*
import java.lang.ProcessBuilder.Redirect.to

/**
 * A simple [Fragment] subclass.
 */
class NotificationsStep2Fragment : BaseFragment(), NotificationsStep2Contract.View {

    @Inject
    lateinit var presenter: NotificationsStep2Contract.Presenter
    private var haveToNavigateHome: Boolean = false
    private var dest: NavigationEntity? = null


    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_notifications_step_2

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        haveToNavigateHome = arguments?.getBoolean(Consts.ARG_HAVE_TO_NAVIGATE_HOME) == true
        dest = arguments?.getParcelable(Consts.ARG_ADVICES_NAVIGATION_ENTITY)
        presenter.setupView(arguments?.getParcelable(Consts.ARG_VERIFICATION))
    }

    override fun setupView() {
        code_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.checkCode(it.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validateCode(text.toString())
                    hideKeyboard()
                }
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    presenter.sendVerificationCode(code_it.editText?.text.toString())
                    true
                }
                false
            }
        }
        verification_step_2_verify_btn.setOnClickListener { presenter.sendVerificationCode(code_it.editText?.text.toString()) }
    }

    override fun showValidCode() {
        code_it.isErrorEnabled = false
    }

    override fun showErrorCode() {
        code_it.error = getString(R.string.error_verification_code)
    }

    override fun close() {
        if (haveToNavigateHome) {
            val bundle = bundleOf(Consts.ARG_ADVICES_NAVIGATION_ENTITY to dest)
            findNavController().navigate(R.id.action_notifications_step_2_dest_to_home_dest, bundle)
        } else {
            findNavController().navigate(R.id.action_notifications_step_2_dest_to_preferences_dest)
        }
    }

    override fun onMessageReceived(messageText: String) {
        code_it.editText?.setText(messageText.trim().takeLast(6))
    }
}
