package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import android.os.Build
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity

/**
 * Get device info use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class GetDeviceInfoUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        result.device.product = Build.PRODUCT
        result.device.manufacturer = Build.MANUFACTURER
        result.device.brand = Build.BRAND
        result.device.model = Build.MODEL
        result.device.baseOs = Build.VERSION.INCREMENTAL
        result.device.codeName = Build.VERSION.SDK_INT.toString()
        result.device.releaseVersion = Build.VERSION.RELEASE
        result.device.securityPatch = Build.VERSION.SECURITY_PATCH
    }
}