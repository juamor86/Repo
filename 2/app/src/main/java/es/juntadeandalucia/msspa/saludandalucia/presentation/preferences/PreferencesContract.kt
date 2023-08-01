package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedContract

class PreferencesContract {

    interface View : LoggedContract.View {
        fun setupSwitch()
        fun enableNotificationsSwitch()
        fun disableNotificationsSwitch()
        fun setNotificationsPermissionSwitch(checked:Boolean)
        //fun setVideocallPermissionSwitch(checked:Boolean)
        //fun setContactPermissionSwitch(checked:Boolean)
        fun setCameraPermissionSwitch(checked:Boolean)
        fun navigateToVerification()
        fun showConfirmDialog()
        fun showNotificationsPhone(phone: String)
        fun hideNotificationsPhone()
        fun showOnBoarding()
        fun showUnsubscriptionErrorDialog()
        fun checkPermissions()
        fun showNotLoggedError()
        fun showNotificationsNotEnabled()
        fun goToConfiguration()
        //fun requestContactPermission()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate()
        fun onResume()
        fun onNotificationsEnabled()
        fun disableNotifications()
        fun onNotificationsDisabled()
        fun onCancelDisableNotifications()
        //fun shouldRequestPermissionRationale()
        //fun shouldNotRequestPermissionRationale()
    }
}
