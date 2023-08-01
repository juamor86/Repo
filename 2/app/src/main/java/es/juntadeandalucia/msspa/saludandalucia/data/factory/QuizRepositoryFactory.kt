package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.QuizRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.QuizRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.QuizRepository

class QuizRepositoryFactory(
    private val quizRepositoryMockImpl: QuizRepositoryMockImpl,
    private val quizRepositoryNetworkImpl: QuizRepositoryNetworkImpl
) : BaseRepositoryFactory<QuizRepository>() {

    override fun create(strategy: Strategy): QuizRepository =
        when (strategy) {
            Strategy.NETWORK -> quizRepositoryNetworkImpl
            else -> quizRepositoryMockImpl
        }
}
