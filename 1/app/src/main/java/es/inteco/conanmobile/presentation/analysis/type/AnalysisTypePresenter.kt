package es.inteco.conanmobile.presentation.analysis.type

import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.domain.usecases.SaveDefaultAnalysisUseCase
import es.inteco.conanmobile.presentation.base.BasePresenter
import timber.log.Timber

/**
 * Analysis type presenter
 *
 * @property getConfigurationUseCase
 * @property saveDefaultAnalysisUseCase
 * @property getDefaultAnalysisUseCase
 * @constructor Create empty Analysis type presenter
 */
class AnalysisTypePresenter(
    private val getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>,
    private val saveDefaultAnalysisUseCase: CompletableUseCase<SaveDefaultAnalysisUseCase.Params>,
    private val getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>
) : BasePresenter<AnalysisTypeContract.View>(), AnalysisTypeContract.Presenter {

    override fun onCreateView() {
        getDefaultAnalysisUseCase.execute(onSuccess = { defaultAnalysis ->
            view.apply {
                getConfigurationUseCase.execute(
                    onSuccess = { configuration ->
                        view.apply {
                            refillRecyclerView(configuration.message.analysis, defaultAnalysis)
                        }
                    }, onError = {
                        Timber.e("Error getting configuration: ${it.message}")
                    })
            }
        }, onError = {
            Timber.e("Error getting default analysis saved id: ${it.message}")
        })
    }

    override fun saveDefaultAnalysis(analysisType: AnalysisEntity) {
        saveDefaultAnalysisUseCase.execute(
            params = SaveDefaultAnalysisUseCase.Params(analysisType),
            onComplete = {
                Timber.d("Id save successfully")
            },
            onError = {
                Timber.e("Error saving id: ${it.message}")
            }
        )
    }

    override fun unsubscribe() {
        getConfigurationUseCase.clear()
        saveDefaultAnalysisUseCase.clear()
        getDefaultAnalysisUseCase.clear()
    }

}