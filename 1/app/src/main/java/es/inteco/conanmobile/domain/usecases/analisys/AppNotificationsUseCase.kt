package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber

/**
 * App notifications use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class AppNotificationsUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        val pm = context.packageManager
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)

        enabledListeners.forEach {
            val itAppInfo = pm.getApplicationInfo(it, PackageManager.GET_META_DATA)
            if ((itAppInfo.flags and ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                result.device.isAppNotificationsVisible = 1
                itemResult.notOk = true
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}" )
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}