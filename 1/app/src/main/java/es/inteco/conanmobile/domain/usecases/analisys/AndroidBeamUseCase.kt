package es.inteco.conanmobile.domain.usecases.analisys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.provider.Settings
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import timber.log.Timber

/**
 * Android beam use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class AndroidBeamUseCase (
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result){
    override fun analyse() {
        val nfcManager : NfcManager  = context.getSystemService(Context.NFC_SERVICE) as NfcManager
        val nfcAdapter = nfcManager.defaultAdapter
        if (nfcAdapter == null) {
            result.device.isAndroidBeam = 0
            itemResult.notOk = false
        }else{
            if (nfcAdapter.isEnabled && nfcAdapter.isNdefPushEnabled) {
                result.device.isAndroidBeam = 1
                itemResult.notOk = true
            }else {
                result.device.isAndroidBeam = 0
                itemResult.notOk = false
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}" )
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}