package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizMapper {

    companion object {

        private val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())

        fun convert(model: QuizData) = with(model) {
            val date = Date(nextTry)
            QuizEntity(
                available = available,
                nextTry = formatter.format(date),
                questions = QuizQuestionMapper.convert(questions),
                appointment = appointment?.let { AppointmentMapper.convert(it) }
            )
        }
    }
}
