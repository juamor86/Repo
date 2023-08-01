package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionsRequestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizResultData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import io.reactivex.Single

interface QuizRepository {
    fun getQuizQuestions(quizSession: QuizSession): Single<QuizData>
    fun sendQuizQuestions(
        phoneNumber: String,
        isHealthProf: Boolean,
        isSecurityProf: Boolean,
        isSpecialProf: Boolean,
        quizQuestionsRequestData: QuizQuestionsRequestData,
        quizSession: QuizSession
    ): Single<QuizResultData>
}
