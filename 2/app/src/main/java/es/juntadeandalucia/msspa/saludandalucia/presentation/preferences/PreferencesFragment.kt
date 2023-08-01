package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.dialog.PreferencesOnBoardingDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.checkIsBackgroundRestricted
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.checkIsCameraPermissionGranted
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.checkIsVideocallPermissionGranted
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.denyNotificationPermission
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.navigateDetailSettingApp
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.requestDetailSettingsApp
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.requestNotificationPermission
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.requestVideocallPermission
import kotlinx.android.synthetic.main.fragment_preferences.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class PreferencesFragment : LoggedFragment(), PreferencesContract.View {

    companion object {
        private const val ON_BOARDING_DIALOG_TAG = "onBoardingDialog"
    }

    @Inject
    lateinit var presenter: PreferencesContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_preferences

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onCreate()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showOnBoarding() {
        val onBoardingDialog = PreferencesOnBoardingDialog()
        activity?.supportFragmentManager?.apply {
            onBoardingDialog.show(this, ON_BOARDING_DIALOG_TAG)
        }
    }

    override fun setupSwitch() {
        notifications_sw.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                if (isChecked) {
                    presenter.onNotificationsEnabled()
                } else {
                    presenter.onNotificationsDisabled()
                }
            }
        }

        pref_notification_permission_sw.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                if (isChecked) {
                    requestNotificationPermission(requireContext())
                } else {
                    denyNotificationPermission(requireContext())
                }
            }
        }

        /*pref_videocall_permission_sw.setOnCheckedChangeListener { view, _ ->
            if (view.isPressed) {
                requestVideocallPermission(requireContext())
            }
        }*/

        /*contact_permission_sw.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                if (isChecked) {
                    checkContactsPermissions()
                } else {
                    goToConfiguration()
                }
            }
        }*/

        camera_permission_sw.setOnCheckedChangeListener { view, _ ->
            if (view.isPressed) {
                requestDetailSettingsApp(requireContext())
            }
        }
    }

    /*fun checkContactsPermissions() {
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) ->
                presenter.shouldRequestPermissionRationale()
            else ->
                presenter.shouldNotRequestPermissionRationale()
        }
    }*/

    override fun goToConfiguration() {
        navigateDetailSettingApp(requireContext())
    }

    /*override fun requestContactPermission() {
        permissionsLauncher.launch(Manifest.permission.READ_CONTACTS)
    }*/

    /*private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setContactPermissionSwitch(true)
            }
        }*/

    override fun showConfirmDialog() {
        showDialog(
            title = R.string.notificaions_confirm_dialog_title,
            message = R.string.notificaions_confirm_dialog_message,
            positiveText = R.string.yes,
            onAccept = {
                presenter.disableNotifications()
            },
            cancelText = R.string.no,
            onCancel = {
                presenter.onCancelDisableNotifications()
            }
        )
    }

    override fun enableNotificationsSwitch() {
        notifications_sw.isChecked = true
    }

    override fun disableNotificationsSwitch() {
        notifications_sw.isChecked = false
    }

    override fun setNotificationsPermissionSwitch(checked: Boolean) {
        pref_notification_permission_sw.isChecked = checked
    }

    /*override fun setVideocallPermissionSwitch(checked: Boolean) {
        pref_videocall_permission_sw.isChecked = checked
    }*/


    /*override fun setContactPermissionSwitch(checked: Boolean) {
        contact_permission_sw.isChecked = checked
    }*/

    override fun setCameraPermissionSwitch(checked: Boolean) {
        camera_permission_sw.isChecked = checked
    }

    override fun showNotificationsPhone(phone: String) {
        notifications_phone_tv.text = getString(R.string.preferences_notifications_phone, phone)
        notifications_phone_tv.visibility = View.VISIBLE
        notifications_tv.visibility = View.GONE
    }

    override fun hideNotificationsPhone() {
        notifications_tv.visibility = View.VISIBLE
        notifications_phone_tv.visibility = View.GONE
    }

    override fun navigateToVerification() {
        findNavController().navigate(R.id.action_preferences_dest_to_notifications_step_1_dest)
    }

    override fun showUnsubscriptionErrorDialog() {
        showErrorDialog(R.string.error_unsubscription_notifications)
    }

    override fun checkPermissions() {
        val notificationPermissionGranted = checkIsBackgroundRestricted(requireContext())
        val cameraPermissionGranted = checkIsCameraPermissionGranted(requireContext())

        //val videocallPermissionGranted = checkIsVideocallPermissionGranted(requireContext())
        //TODO reset contacts
        //val contactPermissionGranted = checkIsContactPermissionGranted(requireContext())


        setNotificationsPermissionSwitch(notificationPermissionGranted)
        setCameraPermissionSwitch(cameraPermissionGranted)
        //setVideocallPermissionSwitch(videocallPermissionGranted)
        //setContactPermissionSwitch(contactPermissionGranted)
    }

    override fun showNotLoggedError() {
        showErrorDialog(R.string.user_not_logged)
    }

    override fun showNotificationsNotEnabled() {
        showConfirmDialog(
            title = R.string.must_enable_notifications_to_aadvice,
            onAccept = { presenter.onNotificationsEnabled() },
            onCancel = { })
    }
}
