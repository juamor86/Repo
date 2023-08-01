package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringDetailData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringQuestionAnwseredData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringProgramDetailEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringProgramQuestionAnwseredEntity

class MonitoringProgramDetailMapper {

    companion object {

        fun convert(model: MonitoringDetailData): MonitoringProgramDetailEntity =
            with(model) {
                MonitoringProgramDetailEntity(
                    title = title,
                    detailText = detailText,
                    date = date,
                    hour = hour,
                    questions = questions.map { convert(it) }
                )
            }

        fun convert(questions: List<MonitoringQuestionAnwseredData>) =
            questions.mapNotNull { convert(it) }

        fun convert(model: MonitoringQuestionAnwseredData): MonitoringProgramQuestionAnwseredEntity =
            with(model) {
                when (type) {
                    MonitoringQuestionAnwseredData.BOOLEAN_TYPE -> MonitoringProgramQuestionAnwseredEntity.BooleanQuestionEntity(
                        questionId = questionId,
                        question = question,
                        answerBoolean = answerBoolean
                    )
                    MonitoringQuestionAnwseredData.DECIMAL_TYPE -> MonitoringProgramQuestionAnwseredEntity.DecimalQuestionEntity(
                        questionId = questionId,
                        question = question,
                        answerText = answerText
                    )
                    MonitoringQuestionAnwseredData.TEXT_TYPE -> MonitoringProgramQuestionAnwseredEntity.TextQuestionEntity(
                        questionId = questionId,
                        question = question,
                        answerText = answerText
                    )
                    MonitoringQuestionAnwseredData.OPTIONS_TYPE -> MonitoringProgramQuestionAnwseredEntity.OptionsQuestionEntity(
                        questionId = questionId,
                        question = question,
                        options = options
                    )
                    MonitoringQuestionAnwseredData.SINGLE_OPTIONS_TYPE -> MonitoringProgramQuestionAnwseredEntity.SingleOptionButtonQuestionEntity(
                        questionId = questionId,
                        question = question,
                        options = options
                    )
                    MonitoringQuestionAnwseredData.MULTIPLE_OPTIONS_TYPE -> MonitoringProgramQuestionAnwseredEntity.MultipleOptionsQuestionEntity(
                        questionId = questionId,
                        question = question,
                        options = options
                    )

                    else -> null!!
                }
            }
    }
}
