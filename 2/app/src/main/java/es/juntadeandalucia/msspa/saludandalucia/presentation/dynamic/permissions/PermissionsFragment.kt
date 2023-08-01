package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.permissions

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.fragment_dyn_permission.*
import kotlinx.android.synthetic.main.fragment_dyn_permission.accept_btn
import kotlinx.android.synthetic.main.view_item_permissions.view.*
import javax.inject.Inject

class PermissionsFragment : BaseFragment(), PermissionsContract.View {

    @Inject
    lateinit var presenter: PermissionsContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_dyn_permission

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onViewCreated(getDynamicScreenEntityArgument())
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun buildScreen(dynamicScreenEntity: DynamicScreenEntity?) {
        dynamicScreenEntity?.let { screen ->
            permission_title.text = screen.title?.text
            for (child in screen.children) {
                val childView = layoutInflater.inflate(R.layout.view_item_permissions, content_ll, false)
                childView.permission_name_tv.text = child.title.text
                childView.permission_subtitle_tv.text = child.navigation.target
                content_ll.addView(childView)
            }
        }
    }

    override fun enableAcceptButton(checked: Boolean) {
        accept_btn.isEnabled = checked
    }

    override fun navigateToHome() {
        findNavController().navigate(R.id.action_permission_to_home_dest)
    }

    override fun onResume() {
        super.onResume()
        presenter.isActivatedPermission()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }


    override fun hideToolbar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun setupViews() {
        permission_agreement_cb.setOnCheckedChangeListener { _, isChecked ->
            run {
                presenter.onSwitchChanged(isChecked)
            }
        }
        accept_btn.setOnClickListener {
            presenter.onUnderstoodClick()
        }
    }

    override fun hideCheckAndButtom() {
        accept_permission_gp.visibility = View.GONE
    }

    override fun displayToolbarOrNo(activated: Boolean) {
        (activity as AppCompatActivity?)?.supportActionBar?.let { actionBar ->
            if (activated) {
                actionBar.show()
            } else {
                actionBar.hide()
            }
        }
    }

    override fun doBackOrNo(activated: Boolean) {
        if(activated){
            closeView()
        }
        //your fragment BackPressed will be disabled when first access.
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            presenter.onBackPressedEvent()
        }
    }

    fun getDynamicScreenEntityArgument(): DynamicScreenEntity? = requireArguments().getParcelable(Consts.ARG_DYNAMIC_LAYOUT)
}
