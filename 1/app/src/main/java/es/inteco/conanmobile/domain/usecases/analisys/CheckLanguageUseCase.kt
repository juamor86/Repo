package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import java.util.*

/**
 * Check language use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class CheckLanguageUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    override fun analyse() {
        Locale.getDefault().language.let { language ->
            result.device.language = language
        }
    }
}