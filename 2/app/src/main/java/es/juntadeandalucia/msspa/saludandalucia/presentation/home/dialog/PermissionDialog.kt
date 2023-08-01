package es.juntadeandalucia.msspa.saludandalucia.presentation.home.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicConsts
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigator
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.HomeContract
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.denyNotificationPermission
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.requestNotificationPermission
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.requestVideocallPermission
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.view_custom_permission_dialog.*

class PermissionDialog(context: Context, val activity: Activity) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_custom_permission_dialog)
        window?.let { Utils.secureAgainstScreenshots(it) }
        window!!.decorView.setBackgroundColor(Color.TRANSPARENT)

        background_permission_sw.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                if (isChecked) {
                    requestNotificationPermission(context)
                } else {
                    denyNotificationPermission(context)
                }
            }
        }

        videocall_permission_sw.setOnCheckedChangeListener { view, _ ->
            if (view.isPressed) {
                requestVideocallPermission(context)
            }
        }

        close_btn.setOnClickListener {
            dismiss()
        }
    }

    fun onResumeDialog(notificationPermission: Boolean, videocallPermission: Boolean) {
        background_permission_sw.isChecked = notificationPermission
        videocall_permission_sw.isChecked = videocallPermission
    }


}