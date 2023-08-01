package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber

/**
 * Check a d b use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class CheckADBUseCase(context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity) :
    BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0).let { adb ->
            result.device.isAdbEnabled = adb
            itemResult.notOk = adb != 0
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}" )
            resultView.showResultMessage(context.getString(R.string.developer_settings_alert))
        }

    }
}