package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.MonitoringRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.MonitoringQuestionMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.MonitoringQuestionMapper.Companion.convertToGroupItemData
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.MonitoringQuestionMapper.Companion.convertToItemData
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import java.util.*
import javax.inject.Inject

class SendMonitoringAnswersUseCase @Inject constructor(private val monitoringRepositoryFactory: MonitoringRepositoryFactory) :
    SingleUseCase<MonitoringListEntity.QuestFilledEntity>() {

    private lateinit var responsesMap: MutableMap<QuestionEntity, MutableList<Any>>
    private lateinit var questionnaire: QuestionnaireEntity

    override fun buildUseCase() = monitoringRepositoryFactory.create(Strategy.NETWORK).run {

        val responses = buildResponses()

        val questionnaireAnswerData = SendNewMonitoringAnswerData(
            item = responses,
            authored = UtilDateFormat.dateTimeToString(Date()),
            questionnaire = "/usuarios/cuestionarios/${questionnaire.id}"
        )

        sendQuestionaireAnswers(questionnaireAnswerData).map {
            questionnaireAnswerData.extension = it.extension
            MonitoringQuestionMapper.convert(
                questionnaireAnswerData
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

    private fun buildQuestion(responses: MutableList<ItemData>, key: QuestionEntity) {
        responses.add(convertToItemData(key, responsesMap[key]!!))
    }

    private fun buildGroup(responses: MutableList<ItemData>, key: QuestionEntity) {
        val groupItem = convertToGroupItemData(key.group!!)
        val index = responses.indexOf(groupItem)
        val questionItem = convertToItemData(key, responsesMap[key]!!)
        if (index != -1) {
            responses[index].item.add(questionItem)
            responses[index].item.sortBy { Integer.parseInt(it.linkId) }
        } else {
            groupItem.item.add(questionItem)
            responses.add(groupItem)
        }
    }


    fun params(
        responsesMap: MutableMap<QuestionEntity, MutableList<Any>>,
        questionnaire: QuestionnaireEntity
    ): SendMonitoringAnswersUseCase =
        this.apply {
            this@SendMonitoringAnswersUseCase.responsesMap = responsesMap
            this@SendMonitoringAnswersUseCase.questionnaire = questionnaire
        }
}
