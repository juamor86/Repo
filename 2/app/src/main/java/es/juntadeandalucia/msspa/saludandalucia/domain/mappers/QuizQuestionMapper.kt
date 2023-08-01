package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizQuestionEntity

class QuizQuestionMapper {

    companion object {

        fun convert(questions: List<QuizQuestionData>) = questions.mapNotNull { convert(it) }

        fun convert(model: QuizQuestionData): QuizQuestionEntity? =
            with(model) {
                when (model.type) {
                    QuizQuestionData.BOOLEAN_TYPE -> QuizQuestionEntity.BooleanQuestionEntity(
                        questionId,
                        question,
                        mandatory
                    )
                    QuizQuestionData.BOOLEAN_EXT_TYPE -> QuizQuestionEntity.BooleanExtQuestionEntity(
                        questionId,
                        question,
                        mandatory
                    )
                    QuizQuestionData.DECIMAL_TYPE -> QuizQuestionEntity.DecimalQuestionEntity(
                        questionId,
                        question,
                        mandatory,
                        minValue!!,
                        maxValue!!
                    )
                    QuizQuestionData.TEXT_TYPE -> QuizQuestionEntity.TextQuestionEntity(
                        questionId,
                        question,
                        mandatory,
                        maxLength!!
                    )
                    QuizQuestionData.OPTIONS_TYPE -> QuizQuestionEntity.OptionsQuestionEntity(
                        questionId,
                        question,
                        mandatory,
                        cardinality!!,
                        options!!
                    )
                    else -> null
                }
            }
    }
}
