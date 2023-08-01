package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.safetynet.SafetyNet
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import okhttp3.internal.notify
import okhttp3.internal.wait


/**
 * Package verified use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class PackageVerifiedUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    companion object {
        private const val GOOGLE_PLAY_SETTINGS_ACTIVITY =".security.settings.VerifyAppsSettingsActivity"
        private const val GOOGLE_PLAY_SETTINGS_COMPONENT = "com.google.android.gms"
    }

    override fun analyse() {
        if(isGooglePlayServicesAvailable()) {
            synchronized(this) {
                SafetyNet.getClient(context).isVerifyAppsEnabled.addOnCompleteListener { task ->
                    synchronized(this@PackageVerifiedUseCase) {
                        if (task.isSuccessful) {
                            val taskResult = task.result
                            itemResult.notOk = !taskResult.isVerifyAppsEnabled
                            result.device.isVerifyApps =
                                if (taskResult.isVerifyAppsEnabled) 1 else 0
                        }
                        this@PackageVerifiedUseCase.notify()
                    }
                }
                wait()
            }
        }
    }

    /**
     * Is google play services available
     *
     * @return
     */
    fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        val intent = Intent()
        intent.setClassName(
            GOOGLE_PLAY_SETTINGS_COMPONENT,
            GOOGLE_PLAY_SETTINGS_COMPONENT + GOOGLE_PLAY_SETTINGS_ACTIVITY
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}