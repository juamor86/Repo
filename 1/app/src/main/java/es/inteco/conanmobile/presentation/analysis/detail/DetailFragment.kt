package es.inteco.conanmobile.presentation.analysis.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseContract
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_analysis_detail.*
import kotlinx.android.synthetic.main.fragment_analysis_detail.advices_cv
import kotlinx.android.synthetic.main.fragment_analysis_detail.whatsapp_cv
import kotlinx.android.synthetic.main.fragment_settings_results.apps_rv
import timber.log.Timber
import javax.inject.Inject

/**
 * Detail fragment
 *
 * @constructor Create empty Detail fragment
 */
class DetailFragment : BaseFragment(), DetailContract.View {

    @Inject
    lateinit var presenter: DetailContract.Presenter
    lateinit var detailAdapter: DetailAdapter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun bindLayout() = R.layout.fragment_analysis_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { _ ->
            presenter.onCreateView(getDetailType(), getDetailData())
            configuration_cv.setOnClickListener {
                presenter.goToDeviceConfiguration()
            }
        }
        advices_cv.setOnClickListener {
            presenter.onNavigateToOSITipsClicked()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initButtons() {
        whatsapp_cv?.setOnClickListener {
            navigateToWhatsapp()
        }
    }

    override fun showWarningIntentWhatsapp() {
        showWarningDialog(message = R.string.label_apps_not_installed,
            positiveText = R.string.accept,
            onAccept = {})
    }

    override fun refillReciclerView(list: List<String>) {
        this.detailAdapter = DetailAdapter(onClickItemListener = { }, onRemoveItemListener = { })

        apps_rv.layoutManager = LinearLayoutManager(context)
        apps_rv.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        apps_rv.adapter = detailAdapter
        detailAdapter.setItemsAndNotify(list)
    }

    override fun fillToolbarTittle(title: String?) {
        val toolbar = activity?.toolbar
        title?.let { text ->
            toolbar?.setTitle(text)
        }
    }

    override fun fillSubTitle(subTitle: String?) {
        subtitle_tv.text = subTitle
    }

    override fun fillIssueIcon(icon: Drawable) {
        incidence_icon_iv.setImageDrawable(icon)
    }

    private fun getDetailType(): String? {
        return arguments?.getString(Consts.ARG_DETAIL_TYPE)
    }

    private fun getDetailData(): MutableList<NetworkEntity> {
        return arguments?.get(Consts.ARG_DETAIL_DATA) as MutableList<NetworkEntity>
    }

    override fun navigateToOSITips() {
        findNavController().navigate(
            R.id.action_analysis_detail_dest_to_osi_dest
        )
    }

    override fun navigateToWhatsapp() {
        context?.let { ct ->
            presenter.lunchWhatsapp(ct)
        }
    }

    override fun navitateToWifiSettings() {
        try {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            requireContext().startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error navigating to WIFI settings: ${ex.message}" )
        }
    }
}