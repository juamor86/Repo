package es.juntadeandalucia.msspa.saludandalucia.presentation.main

import android.content.Context
import android.os.Handler
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.*
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LauncherScreen
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicConsts
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigator
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import timber.log.Timber

class MainPresenter(
    private val getFirstAccessUseCase: GetFirstAccessUseCase,
    private val getNotificationsNotReadedCountUseCase: GetNotificationsNotReadedCountUseCase,
    private val sessionBus: SessionBus,
    private val dynamicUIBus: DynamicUIBus,
    private val navBackPressedBus: NavBackPressedBus,
    private val loginAdvicePressedBus: LoginAdvicePressedBus,
    private val saveFirstAccess: SetFirstAccessUseCase,
    private val launcherScreenBus: LauncherScreenBus,
    private val saveLastUpdateDynReleasesUseCase: SaveLastUpdateDynReleasesUseCase,
    private val getLastUpdateDynReleasesUseCase: GetLastUpdateDynReleasesUseCase
) :
    BasePresenter<MainContract.View>(), MainContract.Presenter {

    private var notificationsCount = 0
    private var pendingNavigationDestination: NavigationEntity? = null
    private var checkedFileValue: Boolean = false
    private var checkedFileReleasesValue: Boolean = false
    private var permissionNav: NavigationEntity? = null
    private lateinit var authManager: MsspaAuthenticationManager

    override fun onCreate(fromUri: Boolean, dynamicNavigator: DynamicNavigator, msspaAuthenticationManager: MsspaAuthenticationManager) {
        authManager = msspaAuthenticationManager
        sessionBus.navigator = dynamicNavigator
        getDynamicReleases()
        getDynamicUI { permissionNavigation ->
            permissionNav = permissionNavigation
            if(!fromUri) {
                launcherScreenBusType(LauncherScreen.LauncherScreenTypes.CHECK_USER_SESSION)
            }
        }
        getNotificationsNotReadCount()
        listenSessionBus()
        listenLauncherBus()
    }

    private fun listenLauncherBus() {
        launcherScreenBus.execute(onNext = { launcherScreen ->
            with(launcherScreen) {
                when (launcherScreenType) {
                    LauncherScreen.LauncherScreenTypes.CHECK_USER_SESSION -> buildLauncherCheckTokenUserSession()
                    LauncherScreen.LauncherScreenTypes.LEGAL_SCREEN -> buildLauncherScreenTypeLegal()
                    LauncherScreen.LauncherScreenTypes.PERMISSIONS_SCREEN -> buildLauncherScreenTypePermissions()
                    LauncherScreen.LauncherScreenTypes.NOTIFICATION_PERMISSION -> buildLauncherScreenTypeNotification()
                    LauncherScreen.LauncherScreenTypes.RELEASES_DIALOG -> buildLauncherScreenTypeReleases()
                }
            }
        }, onError = { Timber.e(it) })
    }

    private fun buildLauncherCheckTokenUserSession(){
        with(sessionBus) {
            if (isRefreshToken()) {
                launchVerificationLogin(this)
            } else {
                checkValidateToken(this)
            }
        }
    }

    override fun onLoginSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        sessionBus.createSession(msspaAuthenticationEntity)
    }

    private fun checkValidateToken(sessionBus: SessionBus){
        sessionBus.buildSessionUser()?.let {
            authManager.msspaTokenManager.validateTokenUseCase.params(it.msspaAuthenticationEntity.accessToken).execute(
                onSuccess = { isValid ->
                    Timber.d("Token isvalid to-> $isValid")
                    sessionBus.performValidateToken(isValid)
                },
                onError = {
                    launcherScreenBusType(LauncherScreen.LauncherScreenTypes.LEGAL_SCREEN)
                    Timber.e(it)
                }
            )
        }?: launcherScreenBusType(LauncherScreen.LauncherScreenTypes.LEGAL_SCREEN)
    }

    private fun listenSessionBus(){
        with(sessionBus) {
            execute(onNext = {
                if (it.isUserAuthenticated()) {
                    onUserLoginNavigation()
                    view.showLoginWelcome(msspaAuthenticationEntity!!.msspaAuthenticationUser!!.prettyName)
                }
                launcherScreen()
            }, onError = {
                launcherScreenBusType(LauncherScreen.LauncherScreenTypes.LEGAL_SCREEN)
                Timber.e(it)
            })
        }
    }

    private fun launcherScreen(){
        if(launcherScreenBus.launcherScreen.launcherScreenType == LauncherScreen.LauncherScreenTypes.CHECK_USER_SESSION){
            launcherScreenBusType(LauncherScreen.LauncherScreenTypes.LEGAL_SCREEN)
        }
    }

    private fun buildLauncherScreenTypeLegal(){
        if (getFirstAccessUseCase.param(Consts.PREF_LEGAL_FIRST_ACCESS).execute()) {
            view.navigateToLegal(permissionNav)
        } else {
            launcherScreenBusType(LauncherScreen.LauncherScreenTypes.PERMISSIONS_SCREEN)
        }
    }

    private fun buildLauncherScreenTypePermissions(){
        if (getFirstAccessUseCase.param(Consts.PREF_PERMISSION_SCREEN_FIRST_ACCESS).execute()) {
            permissionNav?.let {
                view.navigateToPermissions(it)
            } ?: launcherScreenBusType(LauncherScreen.LauncherScreenTypes.NOTIFICATION_PERMISSION)
        } else {
            launcherScreenBusType(LauncherScreen.LauncherScreenTypes.NOTIFICATION_PERMISSION)
        }
    }

    private fun buildLauncherScreenTypeNotification(){
        if (getFirstAccessUseCase.param(Consts.PREF_PERMISSION_NOTIFICATION_FIRST_ACCESS).execute()) {
            view.showPermissionNotification()
            saveFirstAccess.param(Consts.PREF_PERMISSION_NOTIFICATION_FIRST_ACCESS).execute(
                onComplete = {},
                onError = {
                    Timber.e(it)
                }
            )
        } else {
            launcherScreenBusType(LauncherScreen.LauncherScreenTypes.RELEASES_DIALOG)
        }
    }

    private fun buildLauncherScreenTypeReleases(strategy: Strategy = Strategy.FILE) {
        launcherScreenBus.clear()
        dynamicUIBus.getReleases(
            onSuccess = { dynamicReleasesSectionEntity ->
                performReleases(dynamicReleasesSectionEntity)
            }, onError = {
                Timber.e(it)
                //buildLauncherScreenTypeReleases(Strategy.MOCK)
            }, strategy = strategy
        )
    }

    private fun performReleases(dynamicReleasesSectionEntity: DynamicReleasesSectionEntity) {
        val lastUpdatedJson = dynamicReleasesSectionEntity.meta.lastUpdated
        getLastUpdateDynReleasesUseCase.execute(
            onSuccess = { lastUpdatePreferences ->
                if (existLastUpdateNew(lastUpdatePreferences, lastUpdatedJson)) {
                    resetCheckDynamicReleases()
                    view.showDialogReleases(dynamicReleasesSectionEntity)
                } else {
                    checkReleasesNoMoreCheck(dynamicReleasesSectionEntity)
                }
            },
            onError = {
                Timber.e("Error getting last updated dynamic releases")
            }
        )
    }

    private fun resetCheckDynamicReleases() {
        saveFirstAccess
            .param(Consts.PREF_CHECK_DYNAMIC_RELEASES, true)
            .execute(
                onComplete = {
                    Timber.d("Check dynamic releases not checked")
                },
                onError = { Timber.e(it) })
    }

    private fun checkReleasesNoMoreCheck(dynamicReleasesSectionEntity: DynamicReleasesSectionEntity) {
        dynamicReleasesSectionEntity.releases.forEach { parameter ->
            if (parameter.id == DynamicConsts.Releases.DYNAMIC_RELEASES_CHECK_ID) {
                if (getFirstAccessUseCase.param(Consts.PREF_CHECK_DYNAMIC_RELEASES).execute()) { //if the check is not marked
                    view.showDialogReleases(dynamicReleasesSectionEntity)
                }
            }
        }
    }


    override fun onSaveFirstOpenReleases(
        dynamicReleasesSectionEntity: DynamicReleasesSectionEntity,
        isChecked: Boolean
    ) {
        saveCheckDynamicReleases(isChecked)
        saveLastUpdateDynReleasesUseCase.params(dynamicReleasesSectionEntity.meta.lastUpdated)
            .execute(
                onComplete = {
                    Timber.d("LastUpdated dynamic releases save successfully")
                },
                onError = {
                    Timber.e("Error saving LastUpdated dynamic releases: ${it.message}")

                }
            )
    }

    private fun saveCheckDynamicReleases(checked: Boolean){
        if (checked){
            saveFirstAccess
                .param(Consts.PREF_CHECK_DYNAMIC_RELEASES)
                .execute(
                    onComplete = {
                        Timber.d("Check dynamic releases checked")
                    },
                    onError = { Timber.e(it) }
                )
        }
    }

    private fun launcherScreenBusType(launcherScreenTypes: LauncherScreen.LauncherScreenTypes){
        launcherScreenBus.createLauncherScreen(launcherScreenTypes)
    }

    private fun checkLauncherScreenTypeNotification() {
        if(launcherScreenBus.launcherScreen.launcherScreenType == LauncherScreen.LauncherScreenTypes.NOTIFICATION_PERMISSION){
            launcherScreenBusType(LauncherScreen.LauncherScreenTypes.RELEASES_DIALOG)
        }
    }

    override fun onResume() {
        view.checkForUpdates()
        checkLauncherScreenTypeNotification()
    }

    private fun launchVerificationLogin(sessionBus: SessionBus){
        sessionBus.buildSessionUser()?.let {
            view.launchVerificationLogin(it.msspaAuthenticationEntity)
        }
    }

    private fun getNotificationsNotReadCount(){
        getNotificationsNotReadedCountUseCase.execute(
            onNext = {
                notificationsCount = it
                view.updateNotificationsBadge(it)
            },
            onError = { Timber.e(it) },
            onCompleted = {})
    }


    private fun onUserLoginNavigation() {
        view.hideLoading()
        Handler().postDelayed({
            pendingNavigationDestination?.let {navigationEntity->
                if (navigationEntity.navigateAfterLogin) {
                    view.handleNavigation(navigationEntity)
                }
            }
        }, 100)
    }

    private fun getDynamicUI(
        strategy: Strategy = Strategy.NETWORK,
        onSuccess: (NavigationEntity?) -> Unit
    ) {
        dynamicUIBus.getMenu(
            onSuccess = {
                buildDynamicUI(it)
                onSuccess.invoke(getPermissionNavigation(it))
            }, onError = {
                Timber.e(it)
                if (checkedFileValue) {
                    getDynamicUI(Strategy.MOCK, onSuccess = onSuccess)
                } else {
                    checkedFileValue = true
                    getDynamicUI(Strategy.FILE, onSuccess = onSuccess)
                }
            }, strategy = strategy
        )
    }

    private fun getDynamicReleases(strategy: Strategy = Strategy.NETWORK) {
        dynamicUIBus.getReleases(
            onSuccess = {
                Timber.d("Get Dynamic Releases success.")
            }, onError = {
                Timber.e(it)
                if (checkedFileReleasesValue) {
                    getDynamicReleases(Strategy.MOCK)
                } else {
                    checkedFileReleasesValue = true
                    getDynamicReleases(Strategy.FILE)
                }
            }, strategy = strategy
        )
    }

    private fun buildDynamicUI(menu: DynamicMenuEntity) {
        buildMenu(menu[0])
        if(menu.size > 2) {
            buildFabLuncher(menu[2])
        }
        buildMenuFooter(menu[1])
    }

    private fun buildFabLuncher(areaEntity: DynamicAreaEntity) {
        view.buildFabLuncher(areaEntity)
    }

    private fun buildMenu(menu: DynamicAreaEntity) {
        view.buildMenu(menu.children)
    }

    private fun buildMenuFooter(menu: DynamicAreaEntity) {
        view.buildMenuFooter(menu)
    }

    private fun getPermissionNavigation(menu: DynamicMenuEntity): NavigationEntity? {
        menu.map.forEach { dynamicArea ->
            dynamicArea.children.forEach { child ->
                child.navigation?.let { navigation ->
                    if (navigation.target.contains(DynamicConsts.Nav.ACCESS_PERMISSION_INFO)) {
                        return navigation
                    }
                }
            }
        }
        return null
    }

    override fun onUserClicked() {
        view.apply {
            sendEvent(Consts.Analytics.USER_MENU_SCREEN_ACCESS)
            if (sessionBus.msspaAuthenticationEntity?.msspaAuthenticationUser == null) {
                view.launchLogin()
                return
            }
            showUserDialog(
                sessionBus.msspaAuthenticationEntity?.msspaAuthenticationUser,
                notificationsCount
            )
        }

    }

    override fun onLoginClicked() {
        view.launchLogin()
    }

    override fun onLoginForHigherLevelAccessClicked() {
        pendingNavigationDestination?.run {
            view.launchLogin(
                mSSPAAuthEntity = sessionBus.msspaAuthenticationEntity,
                requiredScope = level!!
            )
        }
    }

    override fun onLogoutClicked() {
        view.apply {
            askConfirmationLogout(
                onConfirm = {
                    sessionBus.buildSessionUser()?.let { session ->
                        session.msspaAuthenticationEntity.authorizationToken?.let { authorizationToken ->
                            authManager.msspaTokenManager.invalidateTokenUseCase.params(session.msspaAuthenticationEntity.accessToken, authorizationToken)
                                .execute(
                                    onComplete = {
                                        Timber.d("Token invalidated success.")
                                    },
                                    onError = {
                                        Timber.e(it)
                                    }
                                )
                        }
                    }
                    pendingNavigationDestination = null
                    sessionBus.clearSession()
                }
            )
        }
    }

    override fun onHigherAccessRequired(dest: NavigationEntity) {
        pendingNavigationDestination = dest
        if (sessionBus.msspaAuthenticationEntity?.msspaAuthenticationUser == null) {
            // User is not logged
            onLoginForHigherLevelAccessClicked()
        } else {
            // User is logged but needs authenticate with a higher level
            view.showUserLoggedWithHigherLevelNeededDialog()
        }
    }

    override fun onCancelLoginClicked() {
        pendingNavigationDestination = null
    }

    override fun onLoginError() {
        sessionBus.clearSession()
    }

    override fun clearSession() {
        sessionBus.clearSession()
    }

    override fun updateAvailable() {
        view.showUpdateDialog()
    }

    override fun onUpdateClick(isHMS: Boolean) {
        view.apply {
            if (isHMS) {
                openAppGallery()
            } else {
                openPlayStore()
            }
        }
    }

    override fun unsubscribe() {
        getNotificationsNotReadedCountUseCase.clear()
        sessionBus.clear()
        navBackPressedBus.clear()
        loginAdvicePressedBus.clear()
        launcherScreenBus.clear()
    }

    override fun onAppointmentsMenuClicked(context: Context) {
        checkAppointmentsAppInstalled(context)
    }

    private fun checkAppointmentsAppInstalled(context: Context) {
        if (Utils.checkAppInstalled(context, Consts.APPOINTMENTS_PACKAGE_NAME)) {
            view.openAppointmentsApp()
        } else {
            view.openAppointmentPlayStore()
        }
    }

    override fun onNotificationsClicked() {
        view.navigateToNotifications()
    }

    override fun onPreferencesClicked() {
        view.navigateToPreferences()
    }

    override fun onMenuItemClicked(item: DynamicItemEntity) {
        item.navigation?.let {
            view.apply {
                sendEvent(
                    Consts.Analytics.DYNAMIC_SCREEN_SCREEN_ACCESS + Utils.getDynamicScreenParam(
                        it.target
                    )
                )
                handleNavigation(it)
            }
        }
    }

    override fun onDynamicItemClicked(navigation: NavigationEntity) {
        view.apply {
            sendEvent(
                Consts.Analytics.DYNAMIC_SCREEN_SCREEN_ACCESS + Utils.getDynamicScreenParam(
                    navigation.target
                )
            )
            handleNavigation(navigation)
        }
    }

    override fun onOtherAuthMethodClicked() {
        sessionBus.clearSession()
        view.launchOtherLoginMethods()
    }

    override fun onAcceptNavigateToPlayStore() {
        view.navigateToPlayStore()
    }

    override fun onSessionExpiredAccept() {
        view.navigateToHome()
    }

    override fun onNavPressedBack(isNavPressedBack: Boolean) {
        navBackPressedBus.createNavBackPressed(isNavPressedBack)
    }

    override fun onLoginAdvicePressed() {
        loginAdvicePressedBus.createLoginAdvicePressed()
    }

    override fun pendingNavDestination(){
        var isPendingNavDestination  = false
        pendingNavigationDestination?.let {
            isPendingNavDestination = true
        }
        view.updatePendingNavDestination(isPendingNavDestination)
    }

    override fun resetPendingNavDestination() {
        pendingNavigationDestination = null
    }

    private fun existLastUpdateNew(lastUpdatePreferences: String, lastUpdatedJson: String): Boolean {
        return if (lastUpdatePreferences.isEmpty()) {
            true
        } else {
            try {
                val lastUpdatePreferencesDate = UtilDateFormat.stringToDate(lastUpdatePreferences, UtilDateFormat.DATE_FORMAT_TZ_SLASH)
                val lastUpdatedJsonDate = UtilDateFormat.stringToDate(lastUpdatedJson, UtilDateFormat.DATE_FORMAT_TZ_SLASH)
                lastUpdatePreferencesDate.time < lastUpdatedJsonDate.time
            } catch (e: Exception) {
                false
            }
        }
    }
}
