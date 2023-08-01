package es.juntadeandalucia.msspa.saludandalucia.presentation.legal

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_legal.*

/**
 * A simple [Fragment] subclass.
 */
class LegalFragment : BaseFragment(), LegalContract.View {

    @Inject
    lateinit var presenter: LegalContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout(): Int = R.layout.fragment_legal

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
        initializeEvents()
        with(requireArguments()) {
            presenter.onViewCreated(isFirstAccess = getBoolean(Consts.ARG_FIRST_ACCESS))
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.let { actionBar ->
            if (requireArguments().getBoolean(Consts.ARG_FIRST_ACCESS)) {
                actionBar.hide()
            } else {
                actionBar.show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun hideToolbar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun hideCheckAndButtom() {
        accept_gp.visibility = View.GONE
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    private fun initializeEvents() {
        legal_agreement_cb.setOnCheckedChangeListener { _, isChecked ->
            presenter.onSwitchChanged(isChecked)
        }

        accept_btn.setOnClickListener { presenter.onUnderstoodClick() }
    }

    override fun navigateToHome() {
        findNavController().navigate(R.id.action_legal_to_home_dest)
    }

    override fun navigateToPermission(navigationEntity: NavigationEntity) {
        (requireActivity() as MainActivity).handleNavigation(navigationEntity)
    }

    override fun enableAcceptButton(checked: Boolean) {
        accept_btn.isEnabled = checked
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(!requireArguments().getBoolean(Consts.ARG_FIRST_ACCESS)){
                closeView()
            }
            //your fragment BackPressed will be disabled when first access.
        }
    }

    // endregion
}
