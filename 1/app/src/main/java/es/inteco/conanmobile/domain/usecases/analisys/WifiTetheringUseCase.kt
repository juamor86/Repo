package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber
import java.lang.reflect.Method

/**
 * Wifi tethering use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class WifiTetheringUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    companion object {
        private const val IS_WIFI_AP_ENABLE = "isWifiApEnabled"
        private const val PACKAGE_NAME = "com.android.settings"
        private const val CLASS_NAME = "com.android.settings.TetherSettings"
    }

    override fun analyse() {
        var isEnabled = false
        val wifiManager: WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val isWifiApEnabledMethod: Method = wifiManager.javaClass.getDeclaredMethod(IS_WIFI_AP_ENABLE).apply {
            isAccessible = true
        }
        try {
            isEnabled = isWifiApEnabledMethod.invoke(wifiManager) as Boolean
            if (isEnabled) {
                result.device.isWifiTethering = 1
            } else {
                result.device.isWifiTethering = 0
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        itemResult.notOk = isEnabled
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        val intent = Intent().apply {
            setClassName(PACKAGE_NAME, CLASS_NAME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}