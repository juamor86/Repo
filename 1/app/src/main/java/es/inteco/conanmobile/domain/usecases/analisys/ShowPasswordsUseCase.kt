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
 * Show passwords use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class ShowPasswordsUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        Settings.System.getInt(context.contentResolver, Settings.System.TEXT_SHOW_PASSWORD, 0).let {
            result.device.clearPasswords = it
            itemResult.notOk = it != 0
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent =
                Intent(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Settings.ACTION_PRIVACY_SETTINGS else Settings.ACTION_SECURITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}")
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}