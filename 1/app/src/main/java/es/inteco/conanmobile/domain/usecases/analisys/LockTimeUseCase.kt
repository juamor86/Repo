package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.utils.AnalysisConsts
import timber.log.Timber

/**
 * Lock time use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class LockTimeUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {

    companion object {
        const val MAX_LOCK_TIMEOUT_IN_MILLIS = 60000
    }

    override fun analyse() {
        val timeout =
            Settings.Secure.getLong(context.contentResolver, AnalysisConsts.LOCK_TIME_AFTER_SCREEN_LOCK, 0)
        itemResult.notOk = if (timeout > MAX_LOCK_TIMEOUT_IN_MILLIS) {
            result.device.timeoutToLock = 1
            true
        } else {
            result.device.timeoutToLock = 0
            false
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}")
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}