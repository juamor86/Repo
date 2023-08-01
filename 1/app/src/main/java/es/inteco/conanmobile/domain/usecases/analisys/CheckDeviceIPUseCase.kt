package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.usecases.GetIpBotnetUseCase
import es.inteco.conanmobile.presentation.App
import timber.log.Timber
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Check device i p use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class CheckDeviceIPUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {

    @Inject
    lateinit var getIpBotnetUseCase: GetIpBotnetUseCase

    init {
        App.baseComponent.inject(this)
    }

    override fun analyse() {
        try {
            val ipbotnet = getIpBotnetUseCase.buildUseCase().blockingGet()
            result.device.isBotnet = if (ipbotnet.evidences.isEmpty()) 0 else 1
            itemResult.notOk = ipbotnet.evidences.isNotEmpty()
        } catch (e: RuntimeException) {
            Timber.i("Analysis interrupted")
        } catch (e: Exception) {
            Timber.e("Error analyzing apps - ${e.message}")
        }
    }
}