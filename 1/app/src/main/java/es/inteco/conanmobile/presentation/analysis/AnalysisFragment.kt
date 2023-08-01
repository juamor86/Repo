package es.inteco.conanmobile.presentation.analysis

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.inteco.conanmobile.R
import es.inteco.conanmobile.device.notification.NotificationManager
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.analysis.results.ResultsFragment
import es.inteco.conanmobile.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_analysis_launched.*
import javax.inject.Inject

/**
 * Analysis fragment
 *
 * @constructor Create empty Analysis fragment
 */
class AnalysisFragment : BaseFragment(), AnalysisContract.View {

    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @Inject
    lateinit var presenter: AnalysisContract.Presenter

    override fun bindPresenter(): AnalysisContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun bindLayout() = R.layout.fragment_analysis_launched

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle(R.string.analysis_title)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                presenter.onPermissionsGranted()
            } else {
                presenter.onPermissionsNotGranted()
            }
        }
    }

    override fun checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            presenter.onPermissionsGranted()
        }
    }


    override fun setProgressMax(max: Int) {
        analysis_launched_progressBar.max = max
    }

    override fun updateProgress(progress: Int) {
        view?.postDelayed({
            analysis_launched_progressBar?.progress = progress
        }, progress * 10L)
    }

    override fun setDeviceWarnings(deviceWarnings: Int) {
        settings_launched_numb_incidences_tv?.text = deviceWarnings.toString()
        if (deviceWarnings == 1) {
            settings_launched_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            settings_launched_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.red_dot))
        }
    }

    override fun setAppWarnings(appWarnings: Int) {
        applications_launched_incidences_number_tv?.text = appWarnings.toString()
        if (appWarnings == 1) {
            application_launched_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            applications_launched_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.red_dot))
        }
    }

    override fun setSystemWarnings(systemWarnings: Int) {
        system_incidences_number_tv?.text = systemWarnings.toString()
        if (systemWarnings == 1) {
            system_launched_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            system_launched_dot_iv.setImageDrawable(resources.getDrawable(R.drawable.red_dot))
        }
    }

    override fun showAlertNoNetwork() {
        showErrorDialog(title = R.string.analysis_connection_lost_text, onAccept = {
            view?.postDelayed({ navigateUp() }, 200)
        })

    }

    override fun navigateToResults(
        result: AnalysisResultEntity,
        previousAnalysis: AnalysisResultEntity?
    ) {
        findNavController().navigate(
            R.id.action_analysis_launched_dest_to_analysis_results_dest, bundleOf(
                ResultsFragment.RESULT_ARG to result,
                ResultsFragment.PREVIOUS_ARG to previousAnalysis
            )
        )
    }

    override fun navigateBack() {
        navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showInfoMessage(message: String) {
        showInfoDialog(message)
    }

    override fun sendNotification() {
        notificationManager.sendNotification(getString(R.string.finished_analysis_notification_message))
    }
}