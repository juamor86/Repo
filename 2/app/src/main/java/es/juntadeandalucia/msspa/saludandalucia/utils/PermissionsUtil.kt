package es.juntadeandalucia.msspa.saludandalucia.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

class PermissionsUtil {

    companion object {
        fun checkIsBackgroundRestricted(context: Context): Boolean {
            context.apply {
                val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
                return powerManager.isIgnoringBatteryOptimizations(packageName)
            }
        }

        fun checkIsVideocallPermissionGranted(context: Context): Boolean = Settings.canDrawOverlays(context)


        fun checkIsContactPermissionGranted(context: Context): Boolean =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED


        fun checkIsCameraPermissionGranted(context: Context): Boolean {
            val requiredPermission = android.Manifest.permission.CAMERA
            val checkVal = context.checkCallingOrSelfPermission(requiredPermission)
            return checkVal == PackageManager.PERMISSION_GRANTED
        }

        fun requestVideocallPermission(context: Context) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            ContextCompat.startActivity(
                context,
                intent,
                null
            )
        }

        fun checkCameraPermissionGranted(context: Context): Boolean =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        fun checkReadStoragePermissionGranted(context: Context): Boolean =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        fun checkWriteStoragePermissionGranted(context: Context): Boolean =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED


        fun requestNotificationPermission(context: Context) {
            val intent = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.fromParts("package", context.packageName, null)
            )
            ContextCompat.startActivity(context, intent, null)
        }

        fun denyNotificationPermission(context: Context) {
            val intent = Intent()
            intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            ContextCompat.startActivity(context, intent, null)
        }


        fun navigateDetailSettingApp(context: Context){
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }

        fun requestDetailSettingsApp(context: Context) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${context.packageName}")
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}