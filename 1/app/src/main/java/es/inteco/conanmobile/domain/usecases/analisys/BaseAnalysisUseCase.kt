package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

/**
 * Base analysis use case
 *
 * @property context
 * @property analysisItem
 * @property result
 * @constructor Create empty Base analysis use case
 */
abstract class BaseAnalysisUseCase(
    protected val context: Context,
    val analysisItem: ModuleEntity,
    protected val result: AnalysisResultEntity
) : SingleUseCase<Void, ModuleResultEntity>() {

    protected val itemResult = ModuleResultEntity(analysisItem)

    override fun buildUseCase(params: Void?): Single<ModuleResultEntity> {
        return Single.fromCallable {
            try {
                analyse()
            } catch (e: Exception) {
                Timber.e("Error al ejecutar análisis: %s", analysisItem.code)
                itemResult.failed = true
            }
            itemResult
        }
    }

    /**
     * Analyse
     *
     */
    abstract fun analyse()

    /**
     * Lunch action
     *
     * @param resultView
     */
    open fun lunchAction(resultView: AnalysisResultView) {}

}