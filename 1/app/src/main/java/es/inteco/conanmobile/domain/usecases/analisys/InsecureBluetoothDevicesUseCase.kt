package es.inteco.conanmobile.domain.usecases.analisys

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
import java.lang.reflect.Method

/**
 * Insecure bluetooth devices use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class InsecureBluetoothDevicesUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    @SuppressLint("MissingPermission")
    override fun analyse() {
        result.device.hasInsecureBluetoothDevices = 0
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter = bluetoothManager.adapter ?: return
        with(adapter) {
            itemResult.notOk = if (isEnabled) {
                if (bondedDevices.isNotEmpty()) {
                    val connectedDevice = bondedDevices.firstOrNull { bluetoothDevice ->
                        deviceConnected(bluetoothDevice)
                    }
                    if (connectedDevice != null) {
                        result.device.hasInsecureBluetoothDevices = 1
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } else {
                Timber.i("No Bluetooth devices")
                false
            }
        }
    }

    private fun deviceConnected(bluetoothDevice: BluetoothDevice): Boolean {
        return try {
            val m: Method = bluetoothDevice.javaClass.getMethod("isConnected")
            m.invoke(bluetoothDevice) as Boolean
        } catch (e: Exception) {
            Timber.e("Error checking connected device: ${e.message}")
            false
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