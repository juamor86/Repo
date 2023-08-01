package es.inteco.conanmobile.presentation.analysis.results.apps

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.synthetic.main.fragment_apps_results.*
import javax.inject.Inject


/**
 * Apps fragment
 *
 * @constructor Create empty Apps fragment
 */
class AppsFragment : BaseFragment(), AppsContract.View {
    @Inject
    lateinit var presenter: AppsContract.Presenter
    lateinit var maliciousAdapter: AppsAdapter
    lateinit var permissionAdapter: AppsAdapter

    override fun bindPresenter(): AppsContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun bindLayout() = R.layout.fragment_apps_results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maliciousAdapter = AppsAdapter(presenter::onMaliciousAppClicked, true)
        permissionAdapter = AppsAdapter(presenter::onPermissionAppClicked, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (selector_ll.visibility == View.VISIBLE) {
            critical_btn.setOnClickListener {
                showMaliciousButtonPressed()
                presenter.onMaliciousClicked()
            }
            atention_btn.setOnClickListener {
                showAttentionButtonPressed()
                presenter.onPermissionClicked()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showMaliciousButtonPressed() {
        atention_btn.setBackgroundResource(R.drawable.sh_item_tab_selector)
        critical_btn.setBackgroundResource(R.drawable.sh_item_tab_selector_button_pressed)
        critical_btn.setTextAppearance(R.style.TabButtonSelected)
        atention_btn.setTextAppearance(R.style.TabButtonUnSelected)
    }


    override fun showAttentionButtonPressed() {
        atention_btn.setBackgroundResource(R.drawable.sh_item_tab_selector_button_pressed)
        critical_btn.setBackgroundResource(R.drawable.sh_item_tab_selector)
        atention_btn.setTextAppearance(R.style.TabButtonSelected)
        critical_btn.setTextAppearance(R.style.TabButtonUnSelected)
    }

    override fun setTitleApplications() {
        requireActivity().setTitle(R.string.apps)
    }

    override fun showEmptyView() {
        ok_cl.visibility = View.VISIBLE
        selector_ll.visibility = View.INVISIBLE
    }

    override fun setSubtitle(maliciousAppsDescription: String) {
        val styledText: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(maliciousAppsDescription, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(maliciousAppsDescription)
        }
        apps_subtitle_tv.text = styledText
        apps_subtitle_tv.visibility = View.VISIBLE
    }

    override fun hideSubtitle() {
        apps_subtitle_tv.visibility = View.GONE
    }

    override fun setAppsAnalyzed(size: Int) {
        apps_analyzed_number_tv.text = size.toString()
    }

    override fun hideOk() {
        ok_cl.visibility = View.INVISIBLE
    }

    override fun fillMaliciousList(
        appList: List<ApplicationEntity>
    ) {
        maliciousAdapter.setItemsAndNotify(appList)
        apps_rv.adapter = maliciousAdapter
        apps_rv.visibility = View.VISIBLE
    }

    override fun fillPermissionList(
        appList: List<ApplicationEntity>
    ) {
        permissionAdapter.setItemsAndNotify(appList)
        apps_rv.adapter = permissionAdapter
        apps_rv.visibility = View.VISIBLE
    }

    override fun setDotColorRed() {
        status_dot_iv.setImageResource(R.drawable.red_dot)
    }

    override fun setDotColorGreen() {
        status_dot_iv.setImageResource(R.drawable.green_dot)
    }


    override fun showApp(app: ApplicationEntity) {
        val bundle = bundleOf(
            Consts.ARG_APPLICATION to app
        )
        findNavController().navigate(
            R.id.action_apps_to_permission_dest, bundle
        )
    }

}