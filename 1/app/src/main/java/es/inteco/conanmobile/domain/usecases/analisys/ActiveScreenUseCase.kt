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
 * Active screen use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class ActiveScreenUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        Settings.Global.getInt(context.contentResolver, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, 0)
            .let {
                if (it == 0) {
                    result.device.isStayOnWhileCharging = 0
                } else {
                    result.device.isStayOnWhileCharging = 1
                }
                itemResult.notOk = it != 0
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