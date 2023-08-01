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
 * Dangerous device name use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class DangerousDeviceNameUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        var analysisState = false
        try {
            val deviceName =
                Settings.Global.getString(context.contentResolver, "device_name").lowercase()
            if (deviceName.contains(Build.MODEL.lowercase())
                || deviceName.contains(Build.ID.lowercase())
                || deviceName.contains(Build.PRODUCT.lowercase())
                || deviceName.contains(Build.MANUFACTURER.lowercase())
                || deviceName.contains(Build.BRAND.lowercase())
            ) {
                analysisState = true
                result.device.nameWithoutVersion = 1
            } else {
                analysisState = false
                result.device.nameWithoutVersion = 0
            }
        } catch (e: Exception) {
            Timber.e("Error in riskyDeviceName: ${e.message}")
        }
        itemResult.notOk = analysisState
    }


    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}")
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }

}