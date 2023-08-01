package es.inteco.conanmobile.domain.usecases.analisys

import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.PostAnalysisResultEntity
import es.inteco.conanmobile.domain.mappers.PostAnalysisMapper
import es.inteco.conanmobile.domain.mappers.PostAnalysisResultMapper
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Post analysis result use case
 *
 * @property preferencesRepositoryFactory
 * @property incibeRepositoryFactory
 * @constructor Create empty Post analysis result use case
 */
class PostAnalysisResultUseCase @Inject constructor(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) :
    SingleUseCase<PostAnalysisResultUseCase.Params, PostAnalysisResultEntity>() {

    override fun buildUseCase(params: Params?): Single<PostAnalysisResultEntity> {
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
            .flatMap { device ->
                incibeRepositoryFactory.create(Strategy.NETWORK)
                    .postAnalysisResult(device.message.key, params!!.analysisResultRequestData).map {
                    PostAnalysisResultMapper.convert(it)
                }
            }
    }


    data class Params(val analysisResultEntity: AnalysisResultEntity, val configurationVersion:String, val analysisEntity: AnalysisEntity) {
        val analysisResultRequestData = PostAnalysisMapper.convert(analysisResultEntity, configurationVersion, analysisEntity)
    }
}