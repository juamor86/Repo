package es.juntadeandalucia.msspa.saludandalucia.presentation.apps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.adapter.AppsAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.checkIsHMSActivated
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.checkIsHuaweiDevice
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_apps.*

/**
 * A simple [Fragment] subclass.
 */
class AppsFragment : BaseFragment(), AppsContract.View {

    @Inject
    lateinit var presenter: AppsContract.Presenter

    private lateinit var appsAdapter: AppsAdapter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_apps

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(Utils.isHuawei(requireContext()))
    }

    override fun onDestroyView() {
        hideSearchView()
        super.onDestroyView()
    }

    override fun initializeAppsRecycler() {
        apps_swipe.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                presenter.refreshApps(Utils.isHuawei(requireContext()))
            }
        }
        appsAdapter = AppsAdapter(presenter::onAppClicked, presenter::onButtonPressed)
        apps_rv.adapter = appsAdapter
    }

    /**
    Show list of all apps
     */
    override fun showApps(apps: List<AppEntity>) {
        empty_group?.visibility = View.GONE
        apps_rv?.visibility = View.VISIBLE
        appsAdapter.setItemsAndNotify(apps)
    }

    override fun showAppDetails(
        app: AppEntity,
        itemView: View
    ) {
        val bundle = bundleOf("app_entity" to app)
        val extras =
            FragmentNavigatorExtras(
                // TODO: Perhaps all this strings should be created using the const
                //  IMAGE_HEADER_TRANSITION as it in other parts of the app
                itemView.findViewById<View>(R.id.app_icon_iv) to app.icon,
                itemView.findViewById<View>(R.id.app_name_tv) to app.name,
                itemView.findViewById<View>(R.id.app_category_tv) to app.category,
                itemView.findViewById<View>(R.id.app_description_tv) to app.description,
                itemView.findViewById<View>(R.id.apps_btn) to "download_button"
            )

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(R.id.action_apps_dest_to_app_details_dest, bundle, null, extras)
    }

    override fun downloadApp(app: AppEntity) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.link))
        startActivity(intent)
    }

    override fun openApp(app: AppEntity) {
        val pm = context?.packageManager
        val intent = pm?.getLaunchIntentForPackage(app.packageName)
        startActivity(intent)
    }

    /**
    Turn on the SearchView icon to show that only in this view.
     */
    override fun setupSearchView() {
        // Get the toolbar
        val toolbar = activity?.toolbar
        // Add listener for the SearchView
        toolbar?.search_btn?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                /*
                    Send every character the user write onto the SearchView and notify to the adapter
                    the Data is changed to show the update list of apps
                     */
                override fun onQueryTextChange(newText: String): Boolean {
                    presenter.filterList(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }
            })

            // At last set the icon visible only for APPS fragment
            visibility = View.VISIBLE
        }
    }

    /*
    Turn off the SearchView icon to show that only in this view.
     */
    private fun hideSearchView() {
        val toolbar = requireActivity().toolbar
        toolbar.search_btn.visibility = View.GONE
    }

    override fun showFilteredApps(listApps: List<AppEntity>) {
        appsAdapter.showSearchResults(listApps)
    }

    override fun hideRefreshing() {
        apps_swipe.isRefreshing = false
    }

    override fun showEmptyView() {
        empty_group.visibility = View.VISIBLE
        empty_tv.text = getString(R.string.apps_empty)
        apps_rv.visibility = View.GONE
    }

    override fun showErrorView() {
        empty_group?.visibility = View.VISIBLE
        empty_tv?.text = getString(R.string.apps_error)
        apps_rv?.visibility = View.GONE
    }

    override fun showNoMatchAppsView() {
        empty_group.visibility = View.VISIBLE
        empty_tv.text = getString(R.string.apps_no_result)
        apps_rv.visibility = View.GONE
    }
}
