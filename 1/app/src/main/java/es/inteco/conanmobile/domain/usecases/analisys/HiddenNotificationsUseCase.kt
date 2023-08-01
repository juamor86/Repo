package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import android.content.Intent
import android.provider.Settings
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView

/**
 * Hidden notifications use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class HiddenNotificationsUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        Settings.Secure.getInt(context.contentResolver, "lock_screen_show_notifications")
            .let { showNotifications ->
                itemResult.notOk = if (showNotifications == 0) {
                    result.device.isHiddenNotifications = 0
                    false
                } else {
                    result.device.isHiddenNotifications = 1
                    true
                }
            }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}