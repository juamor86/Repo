package es.inteco.conanmobile.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import java.util.*

/**
 * Application package utils
 *
 * @constructor Create empty Application package utils
 */
class ApplicationPackageUtils {
    companion object {
        val whiteList = whiteList()

        fun getInstalledApps(context: Context): List<ApplicationInfo> {
            val packageManager: PackageManager = context.packageManager
            var packList: List<ApplicationInfo> = packageManager.getInstalledApplications(0)
            packList = packList.filter { info ->
                !whiteList.stream().anyMatch { s: String ->
                    info.packageName.startsWith(s, true)
                }
            }
            return packList
        }

        private fun whiteList(): MutableList<String> {
            return Arrays.asList(
                "es.inteco.conanmobile",
                "com.xiaomi",
                "com.google",
                "com.huawei",
                "com.miui",
                "com.android",
                "com.google",
                "com.huawei",
                "com.xiaomi",
                "com.miui",
                "com.samsung",
                "com.example.android",
                "android"
            )
        }

        fun isAppAvailable(context: Context, appName: String?): Boolean {
            val pm = context.packageManager
            return try {
                pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: NameNotFoundException) {
                false
            }
        }

        fun isApplicationSystem(applicationInfo: ApplicationInfo): Int {
            val isAppSystem: Boolean = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM !== 0
            return if (isAppSystem) 1 else -1
        }
    }
}