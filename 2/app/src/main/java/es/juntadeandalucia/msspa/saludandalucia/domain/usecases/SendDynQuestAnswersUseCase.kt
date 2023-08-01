package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendNewDynQuestAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.DynamicQuestionnairesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.dynamic_questionnaires.DynamicQuestionMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import java.util.*
import javax.inject.Inject

class SendDynQuestAnswersUseCase @Inject constructor(private val dynamicQuestionnairesRepositoryFactory: DynamicQuestionnairesRepositoryFactory) :
    SingleUseCase<DynQuestListEntity.QuestFilledEntity>() {

    private lateinit var responsesMap: MutableMap<DynQuestionEntity, MutableList<Any>>
    private lateinit var questionnaire: DynQuestionnaireEntity

    override fun buildUseCase() =
        dynamicQuestionnairesRepositoryFactory.create(Strategy.NETWORK).run {

            val responses = buildResponses()

            val questionnaireAnswerData = SendNewDynQuestAnswerData(
                item = responses,
                authored = UtilDateFormat.dateTimeToString(Date()),
                questionnaire = "/usuarios/cuestionarios/${questionnaire.id}"
            )

            sendQuestionaireAnswers(questionnaireAnswerData).map {
                DynamicQuestionMapper.convert(
                    it.item, it.authored, it.extension
                )
            }
        }

    private fun buildResponses(): List<ItemData> {
        val responses = mutableListOf<ItemData>()

        for (key in responsesMap.keys) {
            if (key.group != null) {
                buildGroup(responses, key)
            } else {
                buildQuestion(responses, key)
            }
        }
        responses.sortBy { Integer.parseInt(it.linkId) }
        return responses
    }

    private fun buildQuestion(responses: MutableList<ItemData>, key: DynQuestionEntity) {
        responses.add(DynamicQuestionMapper.convertToItemData(key, responsesMap[key]!!))
    }

    private fun buildGroup(responses: MutableList<ItemData>, key: DynQuestionEntity) {
        val groupItem = DynamicQuestionMapper.convertToGroupItemData(key.group!!)
        val index = responses.indexOf(groupItem)
        val questionItem = DynamicQuestionMapper.convertToItemData(key, responsesMap[key]!!)
        if (index != -1) {
            responses[index].item.add(questionItem)
            responses[index].item.sortBy { Integer.parseInt(it.linkId) }
        } else {
            groupItem.item.add(questionItem)
            responses.add(groupItem)
        }
    }


    fun params(
        responsesMap: MutableMap<DynQuestionEntity, MutableList<Any>>,
        questionnaire: DynQuestionnaireEntity
    ): SendDynQuestAnswersUseCase =
        this.apply {
            this@SendDynQuestAnswersUseCase.responsesMap = responsesMap
            this@SendDynQuestAnswersUseCase.questionnaire = questionnaire
        }
}