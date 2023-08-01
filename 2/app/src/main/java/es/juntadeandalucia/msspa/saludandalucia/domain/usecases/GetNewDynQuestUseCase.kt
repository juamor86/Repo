package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.DynamicQuestionnairesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.dynamic_questionnaires.NewDynQuestMapper
import io.reactivex.Single

class GetNewDynQuestUseCase(private val dynQuestRepositoryFactory: DynamicQuestionnairesRepositoryFactory) :
    SingleUseCase<DynQuestionnaireEntity>() {

    private lateinit var id: String

    override fun buildUseCase(): Single<DynQuestionnaireEntity> =
        dynQuestRepositoryFactory.create(Strategy.NETWORK).run {
            getNewDynamicQuest(id).map {
                NewDynQuestMapper.convert(it)
            }
        }

    fun setId(id: String): GetNewDynQuestUseCase {
        this.id = id
        return this
    }
}
