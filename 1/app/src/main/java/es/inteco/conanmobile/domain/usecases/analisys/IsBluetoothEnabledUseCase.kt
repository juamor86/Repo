package es.inteco.conanmobile.domain.usecases.analisys

import android.annotation.SuppressLint
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothManager
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
 * Is bluetooth enabled use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class IsBluetoothEnabledUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    @SuppressLint("MissingPermission")
    override fun analyse() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter = bluetoothManager.adapter ?: return
        with(adapter) {
            if (isEnabled &&
                (getProfileConnectionState(BluetoothHeadset.HEADSET) != BluetoothHeadset.STATE_CONNECTED
                        || getProfileConnectionState(BluetoothA2dp.A2DP) != BluetoothA2dp.STATE_CONNECTED)
            ) {
                result.device.isBleEnabled = 1
                itemResult.notOk = true
            }
        }
    }

    override fun lunchAction(resultView: AnalysisResultView) {
        try {
            val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e("Error launching action: ${ex.message}")
            resultView.showResultMessage(context.getString(R.string.settings_alert))
        }
    }
}