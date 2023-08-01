package es.inteco.conanmobile.domain.usecases.analisys

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.ServiceListener
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.utils.AnalysisConsts.Companion.BLUETOOTHPAN_CLASS
import es.inteco.conanmobile.utils.AnalysisConsts.Companion.IS_TETHERING_ON
import timber.log.Timber
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

/**
 * Bluetooth tethering use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class BluetoothTetheringUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity

) : BaseAnalysisUseCase(context, analysisItem, result) {

    @SuppressLint("PrivateApi", "MissingPermission")
    override fun analyse() {
        val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter = bluetoothManager.adapter ?: return
        try {
            val bluetoothPanClass = Class.forName(BLUETOOTHPAN_CLASS).kotlin
            val constructor = bluetoothPanClass.constructors.elementAt(0)
            val serviceListener = object : ServiceListener {
                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                    // Does nothing
                }

                override fun onServiceDisconnected(profile: Int) {
                    // Does nothing
                }
            }
            constructor.isAccessible = true
            val instance = if (constructor.parameters.size == 2) {
                constructor.call(context, serviceListener)
            } else {
                constructor.call(context, serviceListener, adapter)
            }

            val methodResult =
                bluetoothPanClass.declaredFunctions.single { it.name == IS_TETHERING_ON }
                    .call(instance) as Boolean
            if (methodResult) {
                result.device.isBluetoothTethering = 1
                itemResult.notOk = true
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in bluetooth tethering analysis")
        }
    }


    override fun lunchAction(resultView: AnalysisResultView) {
        val intent = Intent().apply {
            setClassName("com.android.settings", "com.android.settings.TetherSettings")
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}