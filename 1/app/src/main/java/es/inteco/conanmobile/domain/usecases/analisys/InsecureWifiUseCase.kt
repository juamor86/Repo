package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber


/**
 * Insecure wifi use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class InsecureWifiUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    var networkListResult: MutableList<NetworkEntity> = mutableListOf()

    override fun analyse() {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?

        /**
         * Wi
         */
        val wi = wifi!!.connectionInfo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (wi.currentSecurityType == WifiInfo.SECURITY_TYPE_OPEN) {
                result.device.hasInsecureNetwork = 1
                itemResult.notOk = true
                networkListResult.add(NetworkEntity("", wi.ssid, 0))
                itemResult.data = networkListResult
            }
        } else {
            val networkList: List<ScanResult>? = wifi.scanResults
            val currentSSID = wi.ssid.removeSurrounding("\"")
            if (networkList != null) {
                for (network in networkList) {
                    if (currentSSID == network.SSID) {
                        val capabilities: String = network.capabilities
                        if (!capabilities.contains("WPA") || !capabilities.contains("WEP")) {
                            result.device.hasInsecureNetwork = 1
                            itemResult.notOk = true
                            networkListResult.add(NetworkEntity("", currentSSID, 0))
                            itemResult.data = networkListResult
                        }
                    }
                }
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}")
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}