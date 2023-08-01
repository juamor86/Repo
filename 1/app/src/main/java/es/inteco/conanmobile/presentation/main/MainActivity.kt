package es.inteco.conanmobile.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import es.inteco.conanmobile.R
import es.inteco.conanmobile.device.notification.NotificationManager
import es.inteco.conanmobile.di.component.DaggerActivityComponent
import es.inteco.conanmobile.di.module.ActivityModule
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.presentation.analysis.results.ResultsFragment
import es.inteco.conanmobile.presentation.base.BaseActivity
import es.inteco.conanmobile.presentation.home.HomeFragment
import es.inteco.conanmobile.utils.ApplicationPackageUtils
import es.inteco.conanmobile.utils.Consts
import es.inteco.conanmobile.utils.Consts.Companion.TEL017
import es.inteco.conanmobile.utils.Consts.Companion.TELEGRAM_INCIBE
import es.inteco.conanmobile.utils.Consts.Companion.TELEGRAM_PACKAGE_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.ocpsoft.prettytime.PrettyTime
import pl.bclogic.pulsator4droid.library.PulsatorLayout
import java.util.*
import javax.inject.Inject

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
class MainActivity : BaseActivity(), MainContract.View, AnalysisResultView {

    private var warningsButton: MenuItem? = null
    private var osiButton: MenuItem? = null
    private var dropButton: MenuItem? = null
    private var gpsChecked: Boolean = false
    private var havePendingWarnings: Boolean = false

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun bindLayout() = R.layout.activity_main

    override fun injectComponent() {
        DaggerActivityComponent.builder().applicationComponent(App.baseComponent)
            .activityModule(ActivityModule()).build().inject(this)
    }

    override fun bindPresenter() = presenter

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        findNavController(R.id.nav_host_fragment).apply {
            setupActionBarWithNavController(this)
            addOnDestinationChangedListener { _, destination, _ ->
                run {
                    when (destination.id) {
                        R.id.home_dest -> {
                            presenter.onCreate()
                            showToolbarIcons()
                        }
                        else -> hideToolbarIcons()
                    }
                }
            }
        }
        presenter.onCreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun showToolbarIcons() {
        logo_conan_iv?.visibility = View.VISIBLE
        warningsButton?.isVisible = true
        osiButton?.isVisible = true
        dropButton?.isVisible = true
    }

    private fun hideToolbarIcons() {
        logo_conan_iv?.visibility = View.GONE
        warningsButton?.isVisible = false
        osiButton?.isVisible = false
        dropButton?.isVisible = false
    }

    override fun onBackPressed() {
        navigateUp()
    }

    override fun showCloseMessage() {
        showConfirmDialog(title = R.string.warning_running_analysis_title,
            message = R.string.warning_running_analysis_message,
            positiveText = R.string.common_yes,
            cancelText = R.string.common_not,
            onAccept = { navigateUp() },
            onCancel = { })
    }

    override fun close() {
        finish()
    }

    override fun navigateUp() {
        findNavController(R.id.nav_host_fragment).apply {
            if (navigateUp()) {
                Handler().postDelayed({
                    presenter.onNavigateUp()
                }, 5)
            } else {
                close()
            }
        }
    }

    override fun navigateToExternal(url: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse(url)
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        osiButton = menu.findItem(R.id.osi_dest)
        warningsButton = menu.findItem(R.id.warnings_dest)
        dropButton = menu.findItem(R.id.drop_menu_iv)

        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.warnings_dest)?.icon =
            if (havePendingWarnings) AppCompatResources.getDrawable(
                this, R.drawable.ic_pending_alerts
            ) else AppCompatResources.getDrawable(this, R.drawable.ic_alerts)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        navigateUp()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drop_menu_iv -> {
                this.supportFragmentManager.fragments[0]?.childFragmentManager?.fragments?.forEach { fragment ->
                    item.subMenu.findItem(R.id.action_home_dest_to_legal_dest).isVisible =
                        fragment is HomeFragment
                }
            }
            R.id.action_home_dest_to_legal_dest -> presenter.onLegalClicked()
            R.id.menu_help -> presenter.onHelpClicked()
        }

        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(
            item
        )
    }

    override fun navigateToLegalScreen(message: MessageEntity) {
        val bundle = bundleOf(Consts.ARG_CONFIGURATION to message)
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_home_dest_to_legal_dest, bundle
        )
    }

    override fun navigateToHelpScreen() {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_home_dest_to_help_dest
        )
    }

    override fun initControlsWithDefaultAnalysis(analysisEntity: AnalysisEntity) {
        this.supportFragmentManager.fragments[0]?.childFragmentManager?.fragments?.forEach { fragment ->
            if (fragment is HomeFragment) {
                with(fragment.requireView().rootView!!) {
                    scan_ic.start()
                    toolbar?.title = ""
                    logo_conan_iv?.visibility = View.VISIBLE
                    circle_container?.setOnClickListener {
                        presenter.onStartAnalysisClicked()
                    }
                    val messageText: String = getString(
                        R.string.analysis_message, analysisEntity.names[0].value
                    )
                    val styledText: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(messageText, FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(messageText)
                    }
                    analysis_message_tv?.text = styledText
                    whatsapp_btn?.setOnClickListener {
                        navigateToWhatsapp()
                    }
                    telegram_btn?.setOnClickListener {
                        navigateToTelegram()
                    }
                    notifications_btn?.setOnClickListener {
                        navigateToExternal(Consts.INCIBE_NOTIFICATIONS)
                    }
                    last_analysis_iv?.setOnClickListener {
                        presenter.onLastAnalysisClicked()
                    }
                    phone_btn?.setOnClickListener {
                        checkCallPermissions()
                    }
                    incibe_btn?.setOnClickListener {
                        navigateToExternal(Consts.INCIBE_URL)
                    }
                }
            } else {
                logo_conan_iv.visibility = View.GONE
            }
        }
        logo_conan_iv?.setOnClickListener {
            navigateToExternal(Consts.OSI_URL)
        }
    }

    override fun navigateToAnalysis(analysisEntity: AnalysisEntity) {
        logo_conan_iv.visibility = View.GONE
        findViewById<TextView>(R.id.analysis_tv)?.setText(R.string.analyzing_device)
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_home_dest_to_analysis_launched_dest)
    }

    override fun navigateToResults(result: AnalysisResultEntity) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_home_dest_to_analysis_result_dest,
            bundleOf(ResultsFragment.RESULT_ARG to result)
        )
    }

    override fun showAnalysisRunning() {
        showWarningDialog(title = R.string.warning_no_analysis_available_yet_title,
            message = R.string.warning_no_analysis_is_running_message,
            positiveText = R.string.accept,
            onAccept = {})
    }

    override fun showEnableGpsMessage() {
        showConfirmDialog(title = R.string.gps_disabled_title,
            message = R.string.gps_disabled_message,
            positiveText = R.string.accept,
            cancelText = R.string.cancel,
            onAccept = {
                gpsChecked = true
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onCancel = {
                presenter.onStartAnalysisClicked()
            })
    }

    override fun isLocationPermitted(): Boolean {
        return (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Consts.PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION
        )
    }

    override fun navigateToWhatsapp() {
        presenter.lunchWhatsapp(this)
    }

    override fun navigateToTelegram() {
        ApplicationPackageUtils.isAppAvailable(applicationContext, TELEGRAM_PACKAGE_NAME)
            .let { isAppAvailable ->
                if (isAppAvailable) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(TELEGRAM_INCIBE)
                    intent.setPackage(TELEGRAM_PACKAGE_NAME)
                    startActivity(intent)
                } else {
                    showWarningDialog(message = R.string.label_apps_not_installed,
                        positiveText = R.string.accept,
                        onAccept = {})
                }
            }
    }

    override fun showWarningIntentWhatsapp() {
        showWarningDialog(message = R.string.label_apps_not_installed,
            positiveText = R.string.accept,
            onAccept = {})
    }

    override fun showAnalysisUnavailableMessage(message: String) {
        showWarningDialog(title = R.string.warning_no_analysis_available_yet_title,
            messageText = getString(R.string.warning_no_analysis_available_yet_message).replace(
                "%s", message
            ),
            positiveText = R.string.accept,
            onAccept = {})
    }

    override fun showViewLastAnalysis(visible: Boolean) {
        if (visible) {
            findViewById<CardView>(R.id.last_analysis_iv)?.visibility = View.VISIBLE
        } else {
            findViewById<CardView>(R.id.last_analysis_iv)?.visibility = View.GONE
        }
    }

    override fun deactivateAnalysisUntil(time: Long) {
        findViewById<PulsatorLayout>(R.id.scan_ic)?.apply {
            stop()
            circle_container.isClickable = false
            analysis_tv.text = getString(R.string.analysis_not_available)
        }
        val prettyTime =
            PrettyTime(Locale.getDefault()).formatDuration(Date(System.currentTimeMillis() + time))
        val messageText: String = if (prettyTime.isNotEmpty())
            getString(
                R.string.warning_no_analysis_available_yet_message,
                prettyTime
            ) else getString(R.string.warning_no_analysis_available_yet_message_seconds)
        findViewById<TextView>(R.id.analysis_message_tv)?.text = messageText
    }

    override fun errorLaunchedAnalysis(message: String) {
        findViewById<TextView>(R.id.analysis_tv).text = message
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        grantResults[0].let { res ->
            when (requestCode) {
                Consts.PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION -> {
                    if (res == PackageManager.PERMISSION_GRANTED) {
                        presenter.onLocationPermissionGranted()
                    }
                }
                Consts.PHONE_CALL_PERMISSION_REQUEST_CODE -> {
                    if (res == PackageManager.PERMISSION_GRANTED) {
                        call017()
                    } else {
                        showWarningDialog(title = R.string.warning_permissions_needed_title,
                            message = R.string.warning_permissions_needed_message,
                            positiveText = R.string.accept,
                            onAccept = {})
                    }
                }
            }
        }
    }

    private fun call017() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(TEL017)
        startActivity(intent)
    }

    override fun showPendingWarnings(haveNotifications: Boolean) {
        havePendingWarnings = haveNotifications
        invalidateOptionsMenu()
    }

    private fun checkCallPermissions() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.CALL_PHONE), Consts.PHONE_CALL_PERMISSION_REQUEST_CODE
            )
        } else {
            call017()
        }
    }

    override fun showLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            loading_pb?.visibility = View.VISIBLE
        }, 100)
    }

    override fun hideLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            loading_pb?.visibility = View.GONE
        }, 100)
    }

    override fun showResultMessage(message: String) {
        showInfoDialog(message)
    }

    override fun showAlertRecommendedAnalysis() {
        showInfoDialog(R.string.recommended_analysis)
    }
}