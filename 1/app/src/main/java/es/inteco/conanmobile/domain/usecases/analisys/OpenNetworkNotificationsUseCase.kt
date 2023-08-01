package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber

/**
 * Open network notifications use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class OpenNetworkNotificationsUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    /**
     * Analyse
     *
     */
    override fun analyse() {
        Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON
        ).let { wifiNetworksAvailableNotificationOn ->
            itemResult.notOk = if (wifiNetworksAvailableNotificationOn == 0) {
                result.device.isOpenWifiNetworks = 0
                false
            } else {
                result.device.isOpenWifiNetworks = 1
                true
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            var intent = Intent(Settings.ACTION_WIFI_IP_SETTINGS)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}" )
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}