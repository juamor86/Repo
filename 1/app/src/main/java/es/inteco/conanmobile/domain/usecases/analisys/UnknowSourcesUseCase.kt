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
 * Unknow sources use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class UnknowSourcesUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    /**
     * Analyse
     *
     */
    override fun analyse() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            Settings.Global.getInt(
                context.contentResolver, Settings.Global.INSTALL_NON_MARKET_APPS, 0
            ).let { adb ->
                result.device.unknowOrigins = adb
                itemResult.notOk = adb != 0
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            } else {
                Intent(Settings.ACTION_SECURITY_SETTINGS)
            }
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}" )
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}