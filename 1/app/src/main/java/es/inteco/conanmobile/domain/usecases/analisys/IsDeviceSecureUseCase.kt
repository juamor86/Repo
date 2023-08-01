package es.inteco.conanmobile.domain.usecases.analisys

import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView

/**
 * Is device secure use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class IsDeviceSecureUseCase(
    context: Context,
    analysisItem: ModuleEntity,
    result: AnalysisResultEntity,
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        itemResult.notOk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isDeviceLocked()
        } else {
            isPatternSet()
        }
    }

    private fun isDeviceLocked(): Boolean {
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager //api 23+
        if (keyguardManager.isDeviceSecure) {
            result.device.isKeyguardSecure = 1
        } else {
            result.device.isKeyguardSecure = 0
        }
        return !keyguardManager.isDeviceSecure
    }

    private fun isPatternSet(): Boolean {
        val cr: ContentResolver = context.contentResolver
        return try {
            val lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED)
            lockPatternEnable == 1
        } catch (e: Settings.SettingNotFoundException) {
            false
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}