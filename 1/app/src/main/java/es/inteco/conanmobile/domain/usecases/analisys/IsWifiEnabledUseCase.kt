package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.provider.Settings
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber

/**
 * Is wifi enabled use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class IsWifiEnabledUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            val connManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfoWifi: NetworkInfo? =
                connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (networkInfoWifi?.isConnected == false) {
                result.device.isWifiEnabled = 1
                itemResult.notOk = true
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}" )
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}