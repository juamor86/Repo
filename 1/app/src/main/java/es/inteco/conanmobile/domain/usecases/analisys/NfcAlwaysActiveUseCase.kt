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
 * Nfc always active use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class NfcAlwaysActiveUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        try {
            (context.getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter.let { nfcAdapter ->
                result.device.isNfcEnabled = if (nfcAdapter.isEnabled) 1 else 0
                itemResult.notOk =  nfcAdapter.isEnabled
            }
        } catch (e: NullPointerException) {
            Timber.i("this device does not support NFC")
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