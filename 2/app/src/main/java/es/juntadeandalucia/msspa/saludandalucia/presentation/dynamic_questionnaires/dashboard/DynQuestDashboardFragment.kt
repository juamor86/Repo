package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard.onboarding.DynQuestOnBoarding
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.fragment_dyn_quest_dashboard.content_ll
import kotlinx.android.synthetic.main.fragment_dyn_quest_dashboard.text_header_tv
import kotlinx.android.synthetic.main.fragment_dyn_quest_dashboard.view_cl
import kotlinx.android.synthetic.main.view_dynamic_item.view.*
import javax.inject.Inject

class DynQuestDashboardFragment : BaseFragment(), DynQuestDashboardContract.View {

    @Inject
    lateinit var presenter: DynQuestDashboardContract.Presenter
    private lateinit var dynamicScreenEntity: DynamicScreenEntity

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_dyn_quest_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        presenter.onCreate()
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
        val onBoarding = DynQuestOnBoarding()
        activity?.supportFragmentManager?.apply {
            onBoarding.show(this, Consts.ON_BOARDING_DIALOG_TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dynamicScreenEntity = (arguments?.get(Consts.ARG_DYN_QUEST_AVAILIBILITY) as DynamicScreenEntity)
        presenter.onViewCreated(dynamicScreenEntity)
    }

    override fun buildScreen(dynamicScreenEntity: DynamicScreenEntity) {
        text_header_tv?.text  = dynamicScreenEntity.title?.text
        dynamicScreenEntity.background?.let { view_cl?.setBackgroundColor(Color.parseColor(it)) }

        for (child in dynamicScreenEntity.children) {
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
                    presenter.onElementClicked(child)
                }
            }
            content_ll.addView(childView)
        }
    }

    override fun handleNavigation(navigation: NavigationEntity) {
        getNavigator().handleNavigation(navigation)
    }
}