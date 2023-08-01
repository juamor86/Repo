package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicConsts
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard.onboarding.MonitoringOnBoardingDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.fragment_monitoring_on_boarding.*
import javax.inject.Inject

class MonitoringDashboardFragment : BaseFragment(), MonitoringDashboardContract.View {

    @Inject
    lateinit var presenter: MonitoringDashboardContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_monitoring_on_boarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(Consts.ARG_ACCESS_LEVEL)?.let { presenter.setupView(it) }
        with(content_cl) {
            animateViews(
                this,
                animId = R.anim.slide_from_bottom
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.monitoring_on_boarding_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.show_on_boarding) {
            showOnBoardingDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupView() {
        access_btn.setOnClickListener { presenter.onAccessButtonPressed() }
    }

    override fun showOnBoardingDialog() {
        val onBoarding = MonitoringOnBoardingDialog()

        activity?.supportFragmentManager?.apply {
            onBoarding.show(this, Consts.ON_BOARDING_DIALOG_TAG)
        }
    }

    override fun navigateToPrograms(accessLevel: String) {
        val dest = NavigationEntity(
            target = DynamicConsts.Nav.DEST_MONITORING.first,
            type = DynamicConsts.Nav.APP_NATIVE,
            level = accessLevel,
            title = "",
            navigateAfterLogin = true
        )
        getNavigator().handleNavigation(dest)
    }
}