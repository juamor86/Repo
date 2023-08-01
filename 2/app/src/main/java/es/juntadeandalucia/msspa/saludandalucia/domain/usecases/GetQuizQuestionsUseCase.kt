package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.util.Log
import es.juntadeandalucia.msspa.saludandalucia.data.factory.QuizRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.QuizMapper

class GetQuizQuestionsUseCase(private val quizRepositoryFactory: QuizRepositoryFactory) :
    SingleUseCase<QuizEntity>() {

    private lateinit var quizSession: QuizSession

    fun params(quizSession: QuizSession) =
        this.apply {
            this@GetQuizQuestionsUseCase.quizSession = quizSession
        }

    override fun buildUseCase() = quizRepositoryFactory.create(Strategy.NETWORK).run {
        getQuizQuestions(quizSession).map {
            QuizMapper.convert(it)
        }
    }
}
