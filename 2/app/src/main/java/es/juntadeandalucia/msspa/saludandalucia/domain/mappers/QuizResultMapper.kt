package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizResultData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity

class QuizResultMapper {
    companion object {

        fun convert(model: QuizResultData) =
            with(model) {
                when (resultType) {
                    QuizResultData.POSITIVE_RESULT -> QuizResultEntity.QuizResultPositiveEntity(
                        result = result,
                        nextTryMillis = nextTryMillis,
                        appointment = appointment?.let { AppointmentMapper.convert(it) }
                    )
                    QuizResultData.NEGATIVE_RESULT -> QuizResultEntity.QuizResultNegativeEntity(
                        result = result,
                        nextTryMillis = nextTryMillis,
                        appointment = appointment?.let { AppointmentMapper.convert(it) }
                    )
                    else -> null
                }!!
            }
    }
}
