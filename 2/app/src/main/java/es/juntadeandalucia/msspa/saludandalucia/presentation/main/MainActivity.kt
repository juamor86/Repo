package es.juntadeandalucia.msspa.saludandalucia.presentation.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.huawei.agconnect.crash.AGConnectCrash
import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.General.SALUD_ANDALUCIA_APP_KEY_IDENTIFICATION
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerActivityComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.ActivityModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.DialogEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.additions.AdditionsDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.AppsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicActions
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicFeedbackController
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigationController
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigator
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.adapter.MenuAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.dialog.FabDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.dialog.UserDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.HmsTokenProvider
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ANDROID_ID_SO
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.BUILD_TYPE_PRO
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.EVENT_PARAMETER_SEND
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.HUAWEI_ID_SO
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.LINK_SALUD_ANDALUCIA_APP_GALLERY
import es.juntadeandalucia.msspa.saludandalucia.utils.HmsUpdate
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.checkIsHMSActivated
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.checkIsHuaweiDevice
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_drawer_footer.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View, DynamicNavigator,
    MsspaAuthenticationBroadcastReceiver.MsspaAuthenticationCallback,
    DynamicActions {

    private var fabDialog: FabDialog? = null
    private var userDialog: UserDialog? = null
    private var releasesDialog: AdditionsDialog? = null
    private lateinit var authManager: MsspaAuthenticationManager
    private var hmsUpdate = HmsUpdate { updateAvailable() }
    lateinit var barConfig: AppBarConfiguration
    var pendingNavDestination = false
    private var popupLoginWelcomeShown = false

    private val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            //Does nothing
        }

        override fun onDrawerOpened(drawerView: View) {
            sendEvent(Consts.Analytics.DRAWER_SCREEN_ACCESS)
        }

        override fun onDrawerClosed(drawerView: View) {
            //Does nothing
        }

        override fun onDrawerStateChanged(newState: Int) {
            //Does nothing
        }
    }

    @Inject
    lateinit var hmsTokenProvider: HmsTokenProvider

    @Inject
    lateinit var dynamicNavigationController: DynamicNavigationController

    @Inject
    lateinit var dynamicFeedbackController: DynamicFeedbackController

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun bindLayout() = R.layout.activity_main

    override fun injectComponent() {
        DaggerActivityComponent.builder()
            .applicationComponent(App.baseComponent)
            .activityModule(ActivityModule())
            .build()
            .inject(this)
    }

    override fun bindPresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        dynamicFeedbackController.loadEvents()
        dynamicFeedbackController.attachListener(this)
        dynamicNavigationController.navigator = this
        setupActionBar()
        setupNavigationMenu()
        initAuthManager()
        startHmsCheck()
        presenter.onCreate(intent.data != null, this as DynamicNavigator, authManager)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    private fun initAuthManager() {

        val config = MsspaAuthenticationConfig(
            environment = if (BuildConfig.FLAVOR.contains(BUILD_TYPE_PRO)) MsspaAuthenticationConfig.Environment.PRODUCTION else MsspaAuthenticationConfig.Environment.PREPRODUCTION,
            clientId = BuildConfig.API_KEY_IDENTIFICATION,
            apiKey = BuildConfig.API_KEY_IDENTIFICATION,
            appKey = SALUD_ANDALUCIA_APP_KEY_IDENTIFICATION,
            version = BuildConfig.VERSION,
            idSo = if (checkIsHMSActivated(this)) HUAWEI_ID_SO else ANDROID_ID_SO,
            logoToolbar = R.drawable.ic_saludandalucia,
            colorToolbar = R.color.white,
            errorLayout = R.layout.view_custom_auth_confirm_dialog,
            errors = listOf(
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.SEND_NOTIFICATION_ERROR,
                    errorTitle = R.string.send_notification_error_text
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.GENERIC_ERROR,
                    errorTitle = R.string.dialog_error_text
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.LOADING_WEB,
                    errorTitle = R.string.dialog_error_text
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.WRONG_CONFIG,
                    errorTitle = R.string.dialog_error_text
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.NETWORK,
                    errorTitle = R.string.dialog_error_text
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.INVALID_REQUEST,
                    errorTitle = R.string.error_login_fields
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.MAX_ATTEMPTS,
                    errorTitle = R.string.error_max_logins
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.INVALID_PIN_SMS_RECEIVED,
                    errorTitle = R.string.error_login_fields
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.NO_CERTIFICATE,
                    errorTitle = R.string.no_cert_error
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.INVALID_CERTIFICATE,
                    errorTitle = R.string.invalid_cert_error
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.RATE_LIMIT_EXCEEDED,
                    errorTitle = R.string.too_many_request_error_message
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.QR_LOGIN_ERROR,
                    errorTitle = R.string.qr_not_found_login_error
                )
            ),
            warningLayout = R.layout.view_custom_auth_warning_dialog,
            warnings = listOf(
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.OTHER_AUTH_METHODS,
                    warningTitle = R.string.other_auth_methods_message
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.INVALID_BDU_DATA,
                    warningTitle = R.string.error_login_fields
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.INVALID_BDU_PHONE,
                    warningTitle = R.string.error_login_phone
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.PROTECTED_USER,
                    warningTitle = R.string.error_login_protected_user
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.QR_LOGIN_NOT_FOUND_WARNING,
                    warningTitle = R.string.qr_not_found_login_error
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.QR_LOGIN_EXPIRED_WARNING,
                    warningTitle = R.string.qr_expired_login_error
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.NOT_PRIVATE_HEALTH_CARE,
                    warningTitle = R.string.not_private_healt_care
                )
            )
        )

        authManager = MsspaAuthenticationManager(
            context = this, msspaAuthenticationConfig = config
        )
    }

    override fun sendEvent(event: String, onComplete: () -> Unit) {
        super<BaseActivity>.sendEvent(event, onComplete)
        try {
            dynamicFeedbackController.insertEvent(event)
        } catch (e: Exception) {

        }
    }

    private fun showPlayStoreReview() {
        /*val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            } else {
                // There was some problem, log or handle the error code.
                Timber.e(task.exception)
            }
        }*/
    }

    private fun showAppGalleyReview() {
        val intentReview = Intent("com.huawei.appmarket.intent.action.AppDetail")
        intentReview.setPackage("com.huawei.appmarket")
        intentReview.putExtra("APP_PACKAGENAME", "es.juntadeandalucia.msspa.saludandalucia")
        startActivity(intentReview)
    }


    override fun launchReview() {
        if (checkIsHMSActivated(this)) {
            showAppGalleyReview()
        } else {
            navigateToPlayStore()
        }
    }

    private fun launchReviewDialog(title: String, message: String) {
        val reviewDialog = CustomDialog(
            icon = R.drawable.ic_alert_info,
            context = this,
            titleString = title,
            messageString = message,
            negativeText = R.string.not_now_text,
            positiveText = R.string.rate_it_text,
            onAccept = {
                navigateToPlayStore()
            },
            onCancel = {},
            typeDialog = CustomDialog.TypeDialog.INFO
        )

        if (!isFinishing) {
            reviewDialog.show()
        }
    }

    override fun showUserDialog(
        user: MsspaAuthenticationUserEntity?,
        notificationsCount: Int
    ) {
        userDialog?.let {
            it.setUser(user)
        } ?: let {
            userDialog =
                UserDialog(this, user, object : UserDialog.UserDialogListener {
                    override fun onLoginClicked() {
                        presenter.onLoginClicked()
                    }

                    override fun onLogoutClicked() {
                        presenter.onLogoutClicked()
                    }
                })
        }

        userDialog!!.show()
    }

    override fun launchLogin(mSSPAAuthEntity: MsspaAuthenticationEntity?, requiredScope: String) {
        authManager.launch(mSSPAAuthEntity, requiredScope)
    }

    override fun launchVerificationLogin(mSSPAAuthEntity: MsspaAuthenticationEntity) {
        authManager.launchVerificationQRLogin(mSSPAAuthEntity)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        findViewById<DrawerLayout>(R.id.drawer_layout)?.let {
            it.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            barConfig =
                AppBarConfiguration.Builder(
                    R.id.home_dest,
                    R.id.apps_dest
                ).setDrawerLayout(it)
                    .build()

            setupActionBarWithNavController(
                this, findNavController(R.id.nav_host_fragment), barConfig
            )

            it.addDrawerListener(drawerListener)
        }
    }

    private fun setupNavigationMenu() {
        val navController =
            findNavController(R.id.nav_host_fragment).apply {
                addOnDestinationChangedListener { _, destination, bundle ->
                    run {
                        val navHostFragment =
                            supportFragmentManager.primaryNavigationFragment as NavHostFragment?
                        val fragmentManager: FragmentManager? =
                            navHostFragment?.childFragmentManager
                        //TODO REWORK BOTTOMBAR  LISTENERS
                        if (fragmentManager?.primaryNavigationFragment is AppsFragment && this.currentDestination?.label == "fragment_apps") {
                            this.navigate(R.id.action_apps_dest_to_home_dest)
                        } else {
                            when (destination.id) {
                                R.id.home_dest, R.id.apps_dest -> showBottomNavigation()
                                else -> hideBottomNavigation()
                            }
                        }
                    }
                }
            }

        bottom_navigation.apply {
            setupWithNavController(navController)
            itemIconTintList = null
        }

        nav_view.apply {
            setupWithNavController(navController)
        }

        version_tv.text =
            getString(R.string.version_menu, BuildConfig.VERSION_NAME)
    }

    private fun startHmsCheck() {
        val isHuaweiDevice = checkIsHuaweiDevice()
        val isHmsAvailable = checkIsHMSActivated(this)

        if (isHuaweiDevice && isHmsAvailable) {
            AGConnectCrash.getInstance().enableCrashCollection(true)
            hmsTokenProvider.requestToken(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.id

        if (item.itemId == android.R.id.home) {
            when (currentFragment) {
                R.id.advice_detail_dest,
                R.id.dyn_quest_new_dest,
                R.id.monitoring_new_program_dest -> {
                    presenter.onNavPressedBack(true)
                    return true
                }
            }
        }
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(
            item
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(findNavController(R.id.nav_host_fragment), barConfig)
    }

    fun onUserClicked() {
        presenter.onUserClicked()
    }

    private fun hideBottomNavigation() {
        (nav_host_fragment.view?.layoutParams as MarginLayoutParams).bottomMargin = 0
        bottom_navigation_ly.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        (nav_host_fragment.view?.layoutParams as MarginLayoutParams).bottomMargin =
            resources.getDimension(R.dimen.design_bottom_navigation_height).toInt()
        bottom_navigation_ly.visibility = View.VISIBLE
    }

    override fun navigateToLegal(navigationEntity: NavigationEntity?) {
        val bundle = bundleOf(
            Consts.ARG_FIRST_ACCESS to true,
            Consts.ARG_PERMISSION_NAVIGATION to navigationEntity
        )
        findNavController(R.id.nav_host_fragment).navigate(R.id.legal_dest, bundle)
    }

    override fun navigateToHome() {
        findNavController(R.id.nav_host_fragment).popBackStack(R.id.home_dest, false)
    }

    override fun navigateToPermissions(dest: NavigationEntity) {
        handleNavigation(dest)
    }

    override fun openAppointmentsApp() {
        val intent =
            applicationContext.packageManager.getLaunchIntentForPackage(Consts.APPOINTMENTS_PACKAGE_NAME)
        intent!!.addCategory(Intent.CATEGORY_LAUNCHER)
        startActivity(intent)
    }

    override fun openAppointmentPlayStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Consts.APPOINTMENTS_URI)))
    }

    override fun updateNotificationsBadge(notReadedCount: Int) {
    }

    override fun showLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            loading_pb?.visibility = View.VISIBLE
        }, 100)
    }

    override fun showLoadingBlocking() {
        Handler(Looper.getMainLooper()).postDelayed({
            loading_pb?.visibility = View.VISIBLE
            loading_ov?.visibility = View.VISIBLE
        }, 100)

    }

    override fun hideLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            loading_pb?.visibility = View.GONE
            loading_ov?.visibility = View.GONE
        }, 100)
    }

    override fun updateOptionsMenu() {
        invalidateOptionsMenu()
    }

    override fun askConfirmationLogout(onConfirm: () -> Unit) {
        showConfirmDialog(
            title = R.string.logout_menu,
            message = R.string.logout_question,
            onAccept = onConfirm,
            onCancel = {}
        )
    }

    override fun navigateUp() {
        findNavController(R.id.nav_host_fragment).apply {
            navigateUp()
            Handler().postDelayed({
                supportActionBar?.title = currentDestination?.label
            }, 5)
        }
    }

    override fun navigateToWebview(url: String) {
        val bundle = bundleOf(Consts.URL_PARAM to url)
        findNavController(R.id.nav_host_fragment).navigate(R.id.webview_dest, bundle)
    }

    override fun navigateToClicSaludWebview(dest: NavigationEntity) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.clicsalud_webview_dest,
            bundleOf(Consts.URL_PARAM to dest)
        )
    }

    override fun navigateToExternal(url: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }

    override fun navigateToSection(dest: Int, bundle: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({
            with(findNavController(R.id.nav_host_fragment)) {
                if (currentDestination?.id != dest) {
                    navigate(dest, bundle)
                }
            }
        }, 100)
    }



    override fun navigateToNotifications() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.notifications_dest)
    }

    override fun navigateToPreferences() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.preferences_dest)
    }

    override fun navigateToActivateNotifications(dest: NavigationEntity) {
        val bundle = bundleOf(
            Consts.ARG_HAVE_TO_NAVIGATE_HOME to true,
            Consts.ARG_ADVICES_NAVIGATION_ENTITY to dest
        )
        findNavController(R.id.nav_host_fragment).navigate(R.id.notifications_step_1_dest, bundle)
    }

    override fun navigateToDynamicScreen(screen: DynamicScreenEntity) {
        val bundle = bundleOf(Consts.PARAMETER_PARAM to screen)
        findNavController(R.id.nav_host_fragment).navigate(R.id.dynamic_dest, bundle)
    }

    override fun showUserLoggedNeededDialog(
        mSSPAAuthEntity: MsspaAuthenticationEntity?,
        requiredScope: String
    ) {
        showConfirmDialog(
            title = R.string.user_not_logged,
            onAccept = { presenter.onLoginForHigherLevelAccessClicked() },
            onCancel = { presenter.onCancelLoginClicked() }
        )
    }

    override fun showUserLoggedWithHigherLevelNeededDialog() {
        showConfirmDialog(
            title = R.string.user_logged_with_low_permissions,
            onAccept = { presenter.onLoginForHigherLevelAccessClicked() },
            onCancel = { presenter.onCancelLoginClicked() }
        )
    }

    override fun showLoginWelcome(userName: String) {
        if (!popupLoginWelcomeShown) {
            popupLoginWelcomeShown = true
            showSuccessDialog(title = R.string.welcome_login, args = arrayOf(userName), onAccept = {
                presenter.onLoginAdvicePressed()
                presenter.resetPendingNavDestination()
                popupLoginWelcomeShown = false
            })
        }
    }

    override fun showLoginError() {
        showErrorDialog(title = R.string.login_error)
    }

    override fun checkForUpdates() {
        if (checkIsHMSActivated(this)) {
            hmsUpdate.checkHMSUpdate(this)
        } else {
            checkGMSUpdate()
        }
    }

    private fun checkGMSUpdate() {
        // Creates instance of the manager.
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.apply {
            addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    updateAvailable()
                }
            }
            addOnFailureListener {
                Timber.e("Error getting updates: ${it.message}")
            }
        }
    }

    private fun updateAvailable() {
        releasesDialog?.dismiss()
        presenter.updateAvailable()
    }

    override fun showUpdateDialog() {
        showInfoDialog(
            title = R.string.update_avaliable_title,
            message = R.string.update_avaliable_message,
            onAccept = {
                presenter.onUpdateClick(checkIsHMSActivated(this))
            }
        )
    }

    override fun openPlayStore() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

//endregion

// Dynamic UI region

    override fun buildMenu(menu: List<DynamicItemEntity>) {
        val adapter = MenuAdapter(this, menu)
        drawer_menu_lv.setAdapter(adapter)
        drawer_menu_lv.setOnChildClickListener { _, itemView: View, gPos: Int, chPos: Int, _ ->
            for (i in 0 until drawer_menu_lv.count) {
                drawer_menu_lv.collapseGroup(i)
            }
            presenter.onMenuItemClicked(
                adapter.getChild(gPos, chPos)
            )
            drawer_layout.closeDrawers()
            true
        }
        drawer_menu_lv.setOnGroupClickListener { _, itemView: View, gPos: Int, _ ->
            val item = adapter.getGroup(gPos)
            if (item.children.isEmpty()) {
                for (i in 0 until drawer_menu_lv.count) {
                    drawer_menu_lv.collapseGroup(i)
                }
                presenter.onMenuItemClicked(item)
                drawer_layout.closeDrawers()
                true
            } else {
                false
            }
        }
    }

    override fun buildMenuFooter(areaEntity: DynamicAreaEntity) {
        for (child in areaEntity.children) {
            nav_view.apply {
                menu.add(child.title?.text).setOnMenuItemClickListener {
                    child.navigation?.let { handleNavigation(it) }
                    drawer_layout.closeDrawers()
                    true
                }
            }
        }
    }

    override fun handleNavigation(dest: NavigationEntity) {
        dynamicNavigationController.navigateTo(dest)
    }

    override fun higherAccessRequired(dest: NavigationEntity) {
        presenter.onHigherAccessRequired(dest)
    }

    override fun informSectionNotAvailable() {
        showConfirmDialog(
            title = R.string.section_not_available,
            onAccept = { presenter.onAcceptNavigateToPlayStore() },
            onCancel = {}
        )
    }

    override fun openAppGallery() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(LINK_SALUD_ANDALUCIA_APP_GALLERY)
            )
        )
    }

    override fun informSessionExpired() {
        runOnUiThread {
            showConfirmDialog(title = R.string.session_expired, onAccept = {
                presenter.onSessionExpiredAccept()
            })
        }
    }

    override fun informErrorAndHome() {
        runOnUiThread {
            showErrorDialog(onAccept = {
                presenter.onSessionExpiredAccept()
            })
        }
    }

    override fun buildFabLuncher(areaEntity: DynamicAreaEntity) {
        fabDialog ?: run {
            val clickListener: (NavigationEntity) -> Unit = {
                fabDialog?.dismiss()
                presenter.onDynamicItemClicked(it)
            }
            fabDialog = FabDialog(this, areaEntity, clickListener)
        }
    }

    fun showFabDialog(view: View) {
        sendEvent(Consts.Analytics.QUICK_LAUNCHER_SCREEN_ACCESS)
        fabDialog?.show()
    }

    override fun launchOtherLoginMethods() {
        authManager.launchOtherAuthMethods()
    }

    override fun msspaAuthenticationOnError(msspaAuthenticationError: MsspaAuthenticationError) {
        when (msspaAuthenticationError) {
            MsspaAuthenticationError.GENERIC_ERROR,
            MsspaAuthenticationError.LOADING_WEB,
            MsspaAuthenticationError.WRONG_CONFIG,
            MsspaAuthenticationError.NETWORK,
            MsspaAuthenticationError.INVALID_REQUEST,
            MsspaAuthenticationError.QR_LOGIN_ERROR -> showErrorDialog(title = R.string.dialog_error_text)
            MsspaAuthenticationError.SEND_NOTIFICATION_ERROR -> showErrorDialog(title = R.string.send_notification_error_text)
            MsspaAuthenticationError.MAX_ATTEMPTS -> showErrorDialog(title = R.string.error_max_logins)
            MsspaAuthenticationError.INVALID_PIN_SMS_RECEIVED -> showErrorDialog(title = R.string.error_login_fields)
            MsspaAuthenticationError.NO_CERTIFICATE -> showErrorDialog(title = R.string.no_cert_error)
            MsspaAuthenticationError.INVALID_CERTIFICATE -> showErrorDialog(title = R.string.invalid_cert_error)
            MsspaAuthenticationError.RATE_LIMIT_EXCEEDED -> showErrorDialog(title = R.string.too_many_request_error_message)
            MsspaAuthenticationError.CANCEL_LOGIN -> {
                presenter.clearSession()
            }
            MsspaAuthenticationError.ABORT_INIT_LOGIN -> {
                presenter.onCancelLoginClicked()
            }
            MsspaAuthenticationError.SESSION_EXPIRED -> {
                showWarningDialog(
                    title = R.string.session_expired,
                    onAccept = { presenter.clearSession() })
            }
        }
    }

    override fun msspaAuthenticationOnSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        presenter.onLoginSuccess(msspaAuthenticationEntity)
    }

    override fun msspaAuthenticationOnWarning(msspaAuthenticationWarning: MsspaAuthenticationWarning) {
        when (msspaAuthenticationWarning) {
            MsspaAuthenticationWarning.OTHER_AUTH_METHODS -> showInfoDialog(
                R.string.other_auth_methods_message,
                onAccept = {
                    presenter.onOtherAuthMethodClicked()
                })
            MsspaAuthenticationWarning.PIN_MAX_ATTEMPTS -> {
                showWarningDialog(title = R.string.pin_max_attempts)
                presenter.onLoginError()
            }
            MsspaAuthenticationWarning.NOT_PRIVATE_HEALTH_CARE -> {
                showWarningDialog(title = R.string.not_private_healt_care)
            }
        }
    }

    override fun msspaEventLaunch(event: String) {
        sendEvent(Uri.parse(event).getQueryParameters(EVENT_PARAMETER_SEND).first())
    }

    override fun msspaInValidateTokenSuccess() {
    }

    override fun msspaRefreshTokenSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
    }

    override fun msspaValidateTokenSuccess(isSuccess: Boolean) {
    }

    override fun navigateToPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Consts.LINK_SALUD_ANDALUCIA_PLAY_STORE))
        startActivity(intent)
    }

    override fun informNotificationsNotEnabled(dest: NavigationEntity) {
        showConfirmDialog(
            title = R.string.must_enable_notifications_to_aadvice,
            onAccept = { navigateToActivateNotifications(dest) },
            onCancel = { })
    }

    fun getPendingNavigationDestination() {
        presenter.pendingNavDestination()
    }

    override fun updatePendingNavDestination(isPendingNavDestination: Boolean) {
        pendingNavDestination = isPendingNavDestination
    }

    override fun showPermissionNotification() {
        PermissionsUtil.requestNotificationPermission(this)
    }

    override fun showDialogReleases(dynamicReleasesSectionEntity: DynamicReleasesSectionEntity) {
        releasesDialog = AdditionsDialog(onAccept = { isChecked ->
            presenter.onSaveFirstOpenReleases(dynamicReleasesSectionEntity, isChecked)
        }, dynamicReleasesSectionEntity)
        supportFragmentManager.apply {
            if (!isStateSaved && !isDestroyed) {
                sendEvent(Consts.Analytics.ADDITIONS_SHOW)
                releasesDialog?.show(this, Consts.ON_RELEASES_DIALOG_TAG)
            }
        }
    }

    override fun showActionsDialog(dialogEntity: DialogEntity) {
        with(dialogEntity) {
            launchReviewDialog(title, message)
        }
    }

    override fun showDynamicEventsDialog(title: String, message: String, actionId: String) {
        val reviewDialog = CustomDialog(
            icon = R.drawable.ic_alert_info,
            context = this,
            titleString = title,
            messageString = message,
            negativeText = R.string.not_now_text,
            positiveText = R.string.rate_it_text,
            onAccept = {
                makeDynamicEventsAction(actionId)
            },
            onCancel = {},
            typeDialog = CustomDialog.TypeDialog.INFO
        )

        if (!isFinishing) {
            reviewDialog.show()
        }
    }

    override fun makeDynamicEventsAction(action: String) {
        when (action) {
            Consts.ACTION_ME_GUSTA -> launchReview()
            else -> {}
        }
    }
}
