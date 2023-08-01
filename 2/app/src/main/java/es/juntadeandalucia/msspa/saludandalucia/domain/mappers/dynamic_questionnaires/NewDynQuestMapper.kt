package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.dynamic_questionnaires

import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.NewDynQuestData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity

class NewDynQuestMapper {
    companion object {

        fun convert(model: NewDynQuestData): DynQuestionnaireEntity = with(model) {

            val questions = mutableListOf<DynQuestionEntity>()

            for (item in item) {
                val question = DynamicQuestionMapper.convert(item)
                questions.add(question)
                if (item.item.isNotEmpty()) {
                    questions.addAll(item.item.map {
                        DynamicQuestionMapper.convert(
                            it,
                            question
                        )
                    })
                }
            }
            DynQuestionnaireEntity(
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