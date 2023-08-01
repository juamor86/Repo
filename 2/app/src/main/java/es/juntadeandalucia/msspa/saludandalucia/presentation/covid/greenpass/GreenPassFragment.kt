package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.adapter.BeneficiaryAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.onboarding.HubOnBoardingDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomBottomSheetDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_greenpass_certificate.content_ll
import kotlinx.android.synthetic.main.fragment_greenpass_certificate.text_header_tv
import kotlinx.android.synthetic.main.fragment_greenpass_certificate.view_cl
import kotlinx.android.synthetic.main.view_dynamic_item.view.icon_iv
import kotlinx.android.synthetic.main.view_dynamic_item.view.section_tv

class GreenPassFragment : BaseFragment(), GreenPassContract.View {
    private lateinit var beneficiaryDialog: CustomBottomSheetDialog<BeneficiaryEntity>

    @Inject
    lateinit var presenter: GreenPassContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_greenpass_certificate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        presenter.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parameter = arguments?.get(Consts.ARG_DYNAMIC_LAYOUT) as? DynamicScreenEntity
        presenter.onViewCreated(parameter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cert_hub_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.show_on_boarding) {
            showOnBoarding()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showOnBoarding() {
        val onBoarding = HubOnBoardingDialog()
        activity?.supportFragmentManager?.apply {
            onBoarding.show(this, Consts.ON_BOARDING_DIALOG_TAG)
        }
    }

    override fun buildScreen(screen: DynamicScreenEntity) {
        screen.background?.let { view_cl.setBackgroundColor(Color.parseColor(it)) }

        text_header_tv.text = screen.title!!.text
        text_header_tv.setTextColor(Color.parseColor(screen.title.color))

        for (child in screen.children) {
            val childView = layoutInflater.inflate(R.layout.view_dynamic_item, view_cl, false)

            childView.apply {
                section_tv.text = child.title.text

                if (child.icon.source.isNotEmpty()) {
                    icon_iv.visibility = View.VISIBLE
                    Picasso.get().load(child.icon.source).into(icon_iv)
                } else {
                    icon_iv.visibility = View.GONE
                }

                setOnClickListener {
                    with(child) {
                        navigation.bundle =
                            bundleOf(
                                Consts.BUNDLE_CERT_TYPE to navigation.target.split(Consts.SPLIT_URI)[1],
                                Consts.BUNDLE_CERT_TITLE to this.title.alt
                            )
                        presenter.onCertButtonClicked(navigation)
                    }
                }
            }
            content_ll.addView(childView)
        }
    }

    override fun showBeneficiaryList(
        beneficiaryList: MutableList<BeneficiaryEntity>
    ) {
        val adapter = BeneficiaryAdapter(context = requireContext(),
            onClickItemListener = { userEntity, _ ->
                presenter.onSelectBeneficiary(userEntity)
                beneficiaryDialog.dismiss()
            },
            onRemoveItemListener = { }
        ).apply {
            setItemsAndNotify(beneficiaryList)
        }

        beneficiaryDialog = CustomBottomSheetDialog.newInstance(adapter = adapter)
        beneficiaryDialog.show(requireActivity().supportFragmentManager, "")
    }

    override fun navigateToCert(dest: NavigationEntity) {
        getNavigator().handleNavigation(dest)
    }
}
