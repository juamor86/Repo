package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import io.reactivex.Completable

class SaveQuizSessionUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase() {

    private var quizSession: QuizSession? = null

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveQuizSession(quizSession)

    fun params(quizSession: QuizSession): SaveQuizSessionUseCase =
        this.apply {
            this.quizSession = quizSession
        }
}

