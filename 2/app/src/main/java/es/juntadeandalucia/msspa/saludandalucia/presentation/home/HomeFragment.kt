package es.juntadeandalucia.msspa.saludandalucia.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicHomeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicUIHelper
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.apps.adapter.AppsAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert.CertificateOnBoardingDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert.InvalidCertificateDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert.ValidCertificateDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_home.apps_error_tv
import kotlinx.android.synthetic.main.fragment_home.apps_rv
import kotlinx.android.synthetic.main.fragment_home.home_cl
import kotlinx.android.synthetic.main.fragment_home.see_apps_btn
import kotlinx.android.synthetic.main.view_button_user.view.user_iv
import kotlinx.android.synthetic.main.view_button_user.view.user_no_iv

class HomeFragment : BaseFragment(), HomeContract.View {

    companion object {
        internal const val COVID_CERT_DIALOG: String = "cert_dialog"
        internal const val COVID_CERT_ONBOARDING_DIALOG: String = "cert_onboarding_dialog"
    }

    private var loginButton: MenuItem? = null
    private lateinit var appsAdapter: AppsAdapter

    @Inject
    lateinit var presenter: HomeContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_home

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(
            sections = arrayOf(
                getString(R.string.holder),
                getString(R.string.recipients)
            )
        )
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let { ctx ->
            sharedElementReturnTransition =
                TransitionInflater.from(ctx).inflateTransition(android.R.transition.move)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dest: NavigationEntity? = arguments?.getParcelable(Consts.ARG_ADVICES_NAVIGATION_ENTITY)
        activity?.let { dest?.let { (requireActivity() as MainActivity).handleNavigation(dest) } }
        presenter.onViewCreated(Utils.isHuawei(requireContext()))
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this.arguments?.getString(Consts.ARG_VALIDATE_QR_TOKEN))
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun setupListener() {
        see_apps_btn.setOnClickListener { presenter.onAppsButtonPressed() }
    }

    override fun setupAppsRecycler() {
        appsAdapter = AppsAdapter(presenter::onAppClicked)
        apps_rv?.adapter = appsAdapter
    }

    override fun showApps(apps: List<AppEntity>) {
        apps_rv?.visibility = View.VISIBLE
        see_apps_btn?.visibility = View.VISIBLE
        apps_error_tv?.visibility = View.GONE
        appsAdapter.setItemsAndNotify(apps)
    }

    override fun showErrorApps() {
        apps_rv?.visibility = View.GONE
        see_apps_btn?.visibility = View.GONE
        apps_error_tv?.visibility = View.VISIBLE
    }

    override fun navigateToAppDetail(app: AppEntity, itemView: View) {
        val bundle = bundleOf("app_entity" to app)
        val extras =
            FragmentNavigatorExtras(
                itemView.findViewById<View>(R.id.app_home_cv) to app.packageName,
                itemView.findViewById<View>(R.id.app_home_icon_iv) to app.icon
            )
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(R.id.action_home_dest_to_app_details_dest, bundle, null, extras)
    }

    // region - View methods

    override fun navigateToQuiz() {
        findNavController().navigate(R.id.action_home_dest_to_coronavirus_dest)
    }

    override fun navigateToCovidCertificate() {
        findNavController().navigate(R.id.action_home_to_covid_cert_dest)
    }

    override fun navigateToGreenPass() {
        findNavController().navigate(R.id.action_home_to_greenpass_dest)
    }

    override fun navigateToWebview(linkMode: String) {
        val bundle = bundleOf(Consts.URL_PARAM to linkMode)
        findNavController().navigate(R.id.action_home_to_webview, bundle)
    }

    override fun navigateToApps() {
        findNavController().navigate(R.id.apps_dest)
    }

    override fun handleNavigation(navigation: NavigationEntity) {
        getNavigator().handleNavigation(navigation)
    }

    override fun navigateToPreferences() {
        findNavController().navigate(R.id.action_home_dest_to_preferences_dest)
    }

    override fun navigateToExternalBrowser(linkMode: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(linkMode)
            )
        )
    }

    override fun showValidCovidCert(user: UECovidCertEntity) {
        val dialog = ValidCertificateDialog(user)
        activity?.supportFragmentManager?.apply {
            dialog.show(this, COVID_CERT_DIALOG)
        }
    }

    override fun showInvalidCovidCert() {
        val dialog = InvalidCertificateDialog()
        activity?.supportFragmentManager?.apply {
            dialog.show(this, COVID_CERT_DIALOG)
        }
    }

    override fun showValidateCertificateOnboarding() {
        val dialog = CertificateOnBoardingDialog(
            R.layout.view_item_scan_certificate_on_boarding,
            onDismiss = {
                presenter.onDismissedCertificateOnBoardingDialog()
            }
        )
        activity?.supportFragmentManager?.apply {
            dialog.show(this, COVID_CERT_ONBOARDING_DIALOG)
        }
    }

    override fun clearArguments() {
        this.arguments?.clear()
    }

    //endregion

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        loginButton = menu.findItem(R.id.login_dest)
        loginButton!!.actionView.setOnClickListener {
            (requireActivity() as? MainActivity)?.onUserClicked()
        }
        super.onCreateOptionsMenu(menu, inflater)
        presenter.checkUserLogged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login_dest) {
            (requireActivity() as? MainActivity)?.onUserClicked()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showUserLoggedIcon(initials: String) {
        Handler(Looper.getMainLooper()).postDelayed({
        loginButton?.actionView?.findViewById<ImageView>(R.id.user_no_iv)?.visibility =
            View.INVISIBLE
        loginButton?.actionView?.findViewById<TextView>(R.id.user_iv)?.apply {
            visibility = View.VISIBLE
            text = initials
        }
        }, 100)
    }

    override fun showUserNotLoggedIcon() {
        Handler(Looper.getMainLooper()).postDelayed({
            loginButton?.actionView?.apply {
                user_iv.visibility = View.INVISIBLE
                user_no_iv.visibility = View.VISIBLE
            }
        }, 100)
    }

    // dynamic region

    override fun buildDynamicHome(dynamicHomeEntity: DynamicHomeEntity) {
        val homeView = home_cl?.let {
            DynamicUIHelper.getHomeViews(
                requireContext(),
                home_cl,
                dynamicHomeEntity,
                presenter::onDynamicElementClicked
            )
        }

        if (homeView != null) {
            for (view in homeView) {
                home_cl?.addView(view)
            }
        }
    }
}
