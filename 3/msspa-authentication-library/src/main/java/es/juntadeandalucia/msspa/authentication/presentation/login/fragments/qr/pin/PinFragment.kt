package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.pin

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.presentation.dialog.CustomLayoutBottomSheetDialog
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import kotlinx.android.synthetic.main.fragment_pin.*
import javax.inject.Inject

class PinFragment : BaseFragment(), PinContract.View {

    private val HELP_DIALOG = "HELP_DIALOG"

    var authEntity: AuthorizeEntity? = null

    @Inject
    lateinit var presenter: PinContract.Presenter
    override fun bindPresenter(): BaseContract.Presenter = presenter
    override fun bindLayout() = R.layout.fragment_pin

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
        requireArguments().getParcelable<MsspaAuthenticationEntity>(ApiConstants.Arguments.ARG_AUTHENTITY)?.let { authorizeEntity ->
                authEntity = authorizeEntity.authorizeEntity
                presenter.onViewCreated(authorizeEntity)
        }
    }

    override fun setupView() {
        initContinueButton()
        dnie_help_iv.setOnClickListener { presenter.onHelpClicked() }
    }

    override fun showHelp() {
        val environment = (requireActivity() as BaseActivity).authConfig.environment
        val dialog = CustomLayoutBottomSheetDialog(R.layout.view_pin_help, environment)
        activity?.supportFragmentManager?.apply {
            dialog.show(this, HELP_DIALOG)
        }
    }

    private fun initContinueButton() {
        continue_bt.setOnClickListener {
            presenter.onContinueClick(can_it.editText?.text.toString())
        }
    }

    override fun askForPinConfirmation() {
        continue_bt.text = getString(R.string.msspa_auth_confirm)
        code_tv.text = getString(R.string.confirm_pin)
        can_it.editText?.text?.clear()
        view?.scrollY = 0
    }

    override fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun askForPinDefault(firstPin: String) {
        continue_bt.text = getString(R.string.next)
        code_tv.text = getString(R.string.msspa_auth_pin_text)
        can_it.editText?.apply {
            setText(firstPin)
        }
    }

    override fun onNavBackPressed() {
        presenter.performNavBackPressed()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            presenter.performNavBackPressed()
        }
    }

    override fun navigateOnBackPressed(confirmScreen: Boolean, firstPin: String) {
        if (confirmScreen) {
            presenter.setConfirmScreen(false)
            askForPinDefault(firstPin)
        } else {
            val bundle = bundleOf(
                ApiConstants.Arguments.ARG_QR_CLEAN_FORM to true,
                ApiConstants.Arguments.ARG_AUTHORIZE to authEntity
            )
            findNavController().navigate(R.id.action_pin_dest_to_qr_login_dest, bundle)
        }
    }
}