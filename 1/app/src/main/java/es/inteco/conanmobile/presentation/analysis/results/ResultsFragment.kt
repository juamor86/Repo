package es.inteco.conanmobile.presentation.analysis.results

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.ApplicationPackageUtils
import es.inteco.conanmobile.utils.Consts
import es.inteco.conanmobile.utils.Utils
import kotlinx.android.synthetic.main.fragment_analysis_results.*
import javax.inject.Inject

/**
 * Results fragment
 *
 * @constructor Create empty Results fragment
 */
class ResultsFragment : BaseFragment(), ResultsContract.View {
    companion object {
        const val RESULT_ARG = "result"
        const val PREVIOUS_ARG = "previous"
    }

    @Inject
    lateinit var presenter: ResultsContract.Presenter

    override fun bindLayout() = R.layout.fragment_analysis_results

    override fun bindPresenter() = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated(requireArguments()[RESULT_ARG] as AnalysisResultEntity,requireArguments()[PREVIOUS_ARG] as AnalysisResultEntity? )
        advices_cv.setOnClickListener {
            presenter.onNavigateToOSIClicked()
        }
    }

    override fun toolbarTittle() {
        requireActivity().setTitle(R.string.results)
    }

    override fun initButtons() {
        ok_cv.visibility = View.GONE
        settings_results_cv.setOnClickListener { presenter.onDeviceClicked() }
        application_results_cv.setOnClickListener {
            presenter.onAppsClicked()
        }
        system_results_cv.setOnClickListener {
            presenter.onPermissionClicked()
        }
        whatsapp_cv?.setOnClickListener {
            presenter.onNavigateToWhatsapp()
        }
    }

    override fun loadResults(settings: Int, app: Int, perm: Int) {
        settings_numb_incidences_tv.text = settings.toString()
        applications_incidences_number_tv.text = app.toString()
        system_incidences_number_tv.text = perm.toString()
        incidences_tv.setText(R.string.incidences)
        application_incidences_tv.setText(R.string.incidences)
        system_incidences_tv.setText(R.string.incidences)

        if (settings == 0) {
            settings_results_cv.setCardBackgroundColor(resources.getColor(R.color.noIncidence))
            device_analysis_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.green_dot))
        } else {
            settings_results_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            device_analysis_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.red_dot))
        }
        if (app == 0) {
            application_results_cv.setCardBackgroundColor(resources.getColor(R.color.noIncidence))
            applications_analysis_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.green_dot))
        } else {
            application_results_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            applications_analysis_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.red_dot))
        }
        if (perm == 0) {
            system_results_cv.setCardBackgroundColor(resources.getColor(R.color.noIncidence))
            permissions_analysis_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.green_dot))
        } else {
            system_results_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            permissions_analysis_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.red_dot))
        }
    }

    override fun navigateDetailResult(previousAnalysis: AnalysisResultEntity?, result: AnalysisResultEntity, type: ModuleEntity.AnalysisType) {
        val bundle = bundleOf(
            Consts.ARG_RESULT to result, Consts.ARG_DETAIL_TYPE to type,
            Consts.ARG_PREVIOUS to previousAnalysis
        )
        findNavController().navigate(
            R.id.action_analysis_results_dest_to_analysis_settings_dest, bundle
        )
    }

    override fun navigateOSITips() {
        findNavController().navigate(
            R.id.action_analysis_results_dest_to_osi_dest
        )
    }

    override fun navigateToApps() {
        findNavController().navigate(
            R.id.apps_dest
        )
    }

    override fun showWarningIntentWhatsapp() {
        showWarningDialog(message = R.string.label_apps_not_installed,
            positiveText = R.string.accept,
            onAccept = {})
    }

    override fun navigateToWhatsapp() {
        requireContext().let {
            ApplicationPackageUtils.isAppAvailable(it, Consts.WHATSAPP_PACKAGE_NAME)
                .let { isAppAvailable ->
                    if (isAppAvailable) {
                        Utils.goToWhatsapp(it)
                    } else {
                        presenter.whatsappNotInstalled()
                    }
                }
        }
    }
}