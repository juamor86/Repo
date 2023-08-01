package es.inteco.conanmobile.presentation.analysis.results.settings

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.synthetic.main.fragment_settings_results.*
import javax.inject.Inject

/**
 * Settings fragment
 *
 * @constructor Create empty Settings fragment
 */
class SettingsFragment : BaseFragment(), SettingsContract.View {
    //region INITIALIZATION
    @Inject
    lateinit var presenter: SettingsContract.Presenter
    lateinit var settingsAdapter: CriticalSettingsAdapter

    override fun bindPresenter(): SettingsContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun bindLayout() = R.layout.fragment_settings_results

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (selector_ll.visibility == View.VISIBLE) {
            critical_btn.setOnClickListener {
                atention_btn.setBackgroundResource(R.drawable.sh_item_tab_selector)
                critical_btn.setBackgroundResource(R.drawable.sh_item_tab_selector_button_pressed)
                critical_btn.setTextAppearance(R.style.TabButtonSelected)
                atention_btn.setTextAppearance(R.style.TabButtonUnSelected)
                status_dot_iv.setImageResource(R.drawable.red_dot)
                presenter.onCreateView(getPreviousAnalysisResult(), getResult(), getTypeResult())
            }
            atention_btn.setOnClickListener {
                atention_btn.setBackgroundResource(R.drawable.sh_item_tab_selector_button_pressed)
                critical_btn.setBackgroundResource(R.drawable.sh_item_tab_selector)
                atention_btn.setTextAppearance(R.style.TabButtonSelected)
                critical_btn.setTextAppearance(R.style.TabButtonUnSelected)
                status_dot_iv.setImageResource(R.drawable.yellow_dot)
                presenter.onCreateView(getPreviousAnalysisResult(), getResult(), getTypeResult())
            }
            presenter.onCreateView(getPreviousAnalysisResult(), getResult(), getTypeResult())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setToolbarTitleSettings() {
        requireActivity().title = getString(R.string.settings)
    }

    override fun setTitleApplications() {
        requireActivity().setTitle(R.string.apps)
    }

    override fun setTitleSystem() {
        requireActivity().setTitle(R.string.system)
    }

    override fun showWarningIntentWhatsapp() {
        showWarningDialog(message = R.string.label_apps_not_installed,
            positiveText = R.string.accept,
            onAccept = {})
    }

    override fun refillRecyclerView(
        incidencesNamesList: List<ModuleResultEntity>, attentionNamesList: List<ModuleResultEntity>
    ) {

        this.settingsAdapter = CriticalSettingsAdapter(onClickItemListener = { module ->
            presenter.onModuleClicked(module)
        }, onButtonClickListener = { module ->
            presenter.onActionModuleClicked(module, requireActivity() as AnalysisResultView)
        }, onRemoveItemListener = { })

        if (atention_btn.isPressed) {
            numb_incidences_tv.text = attentionNamesList.size.toString()
            settingsAdapter.setItemsAndNotify(attentionNamesList)
        } else {
            numb_incidences_tv.text = incidencesNamesList.size.toString()
            settingsAdapter.setItemsAndNotify(incidencesNamesList)
        }
        apps_rv.layoutManager = LinearLayoutManager(context)
        apps_rv.adapter = settingsAdapter
    }

    override fun navigateToAnalysisDetail(name: String, list: MutableList<NetworkEntity>) {
        val bundle = bundleOf(
            Consts.ARG_DETAIL_TYPE to name, Consts.ARG_DETAIL_DATA to list
        )
        findNavController().navigate(
            R.id.action_analysis_settings_dest_to_analysis_detail_dest, bundle
        )
    }

    private fun getResult(): AnalysisResultEntity {
        return arguments?.get(Consts.ARG_RESULT) as AnalysisResultEntity
    }

    private fun getPreviousAnalysisResult(): AnalysisResultEntity? {
        return arguments?.get(Consts.ARG_PREVIOUS) as AnalysisResultEntity?
    }

    private fun getTypeResult(): ModuleEntity.AnalysisType {
        return arguments?.get(Consts.ARG_DETAIL_TYPE) as ModuleEntity.AnalysisType
    }

    override fun hideOkScreen() {
        selector_ll.visibility = View.VISIBLE
        ok_block_cl.visibility = View.GONE
    }

    override fun showDescription(moduleResultEntity: ModuleResultEntity) {
        val messageText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(
                "<p>${moduleResultEntity.item.assessment.reason.first().value}</p>",
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            Html.fromHtml("<p>${moduleResultEntity.item.assessment.reason.first().value}</p>")
        }
        showWarningDialog(
            messageText = messageText.toString(),
            positiveText = R.string.accept,
            onAccept = {})
    }

}