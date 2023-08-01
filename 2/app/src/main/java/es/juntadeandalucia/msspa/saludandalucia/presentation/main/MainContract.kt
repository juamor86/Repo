package es.juntadeandalucia.msspa.saludandalucia.presentation.main

import android.content.Context
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigator

class MainContract {

    interface View : BaseContract.View {
        fun askConfirmationLogout(onConfirm: () -> Unit)
        fun updateOptionsMenu()
        fun navigateUp()
        fun openAppointmentsApp()
        fun openAppointmentPlayStore()
        fun navigateToLegal(navigationEntity: NavigationEntity?)
        fun navigateToHome()
        fun updateNotificationsBadge(notReadedCount: Int)
        fun launchLogin(
            mSSPAAuthEntity: MsspaAuthenticationEntity? = null,
            requiredScope: String = ""
        )
        fun launchVerificationLogin(mSSPAAuthEntity: MsspaAuthenticationEntity)

        fun showLoginWelcome(userName: String)
        fun showLoginError()
        fun showUserDialog(user: MsspaAuthenticationUserEntity?, notificationsCount: Int)
        fun navigateToNotifications()
        fun navigateToPreferences()
        fun navigateToActivateNotifications(dest: NavigationEntity)
        fun checkForUpdates()
        fun showUpdateDialog()
        fun openPlayStore()
        fun openAppGallery()
        fun buildMenu(menu: List<DynamicItemEntity>)
        fun navigateToDynamicScreen(screen: DynamicScreenEntity)
        fun buildFabLuncher(areaEntity: DynamicAreaEntity)
        fun buildMenuFooter(areaEntity: DynamicAreaEntity)
        fun handleNavigation(navigation: NavigationEntity)
        fun showUserLoggedWithHigherLevelNeededDialog()
        fun launchOtherLoginMethods()
        fun navigateToPlayStore()
        fun updatePendingNavDestination(isPendingNavDestination:Boolean)
        fun navigateToPermissions(dest: NavigationEntity)
        fun showPermissionNotification()
        fun showDialogReleases(dynamicReleasesSectionEntity: DynamicReleasesSectionEntity)
        fun launchReview()

    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(fromUri: Boolean, dynamicNavigator: DynamicNavigator, msspaAuthenticationManager: MsspaAuthenticationManager)
        fun onAppointmentsMenuClicked(context: Context)
        fun onLogoutClicked()
        fun onUserClicked()
        fun onLoginClicked()
        fun onLoginForHigherLevelAccessClicked()
        fun onLoginSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity)
        fun onCancelLoginClicked()
        fun onLoginError()
        fun clearSession()
        fun onNotificationsClicked()
        fun onPreferencesClicked()
        fun updateAvailable()
        fun onUpdateClick(isHMS: Boolean)
        fun onMenuItemClicked(item: DynamicItemEntity)
        fun onDynamicItemClicked(navigation: NavigationEntity)
        fun onHigherAccessRequired(dest: NavigationEntity)
        fun onOtherAuthMethodClicked()
        fun onAcceptNavigateToPlayStore()
        fun onSessionExpiredAccept()
        fun onNavPressedBack(isNavPressedBack: Boolean)
        fun onLoginAdvicePressed()
        fun pendingNavDestination()
        fun resetPendingNavDestination()
        fun onResume()
        fun onSaveFirstOpenReleases(dynamicReleasesSectionEntity: DynamicReleasesSectionEntity, isChecked:Boolean)
    }
}
