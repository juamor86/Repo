package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession

class GetQuizSessionUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<QuizSession?>() {

    override fun execute(): QuizSession? =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getQuizSession()
}
