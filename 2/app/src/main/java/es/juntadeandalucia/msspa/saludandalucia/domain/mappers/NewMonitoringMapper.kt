package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.NewMonitoringProgramData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity

class NewMonitoringMapper {

    companion object {

        fun convert(model: NewMonitoringProgramData): QuestionnaireEntity = with(model) {

            val questions = mutableListOf<QuestionEntity>()

            for (item in item) {
                val question = MonitoringQuestionMapper.convert(item)
                questions.add(question)
                if (item.item.isNotEmpty()) {
                    questions.addAll(item.item.map {
                        MonitoringQuestionMapper.convert(
                            it,
                            question
                        )
                    })
                }
            }
            QuestionnaireEntity(
                title = title,
                date = date,
                id = identifier[0].value,
                code = code,
                extension = extension,
                name = name,
                identifier = identifier,
                status = status,
                purpose = purpose,
                questions = questions
            )
        }
    }
}
