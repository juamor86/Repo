package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.util.Log
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionsRequestData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.QuizRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.QuizResultMapper

class SendQuizResponsesUseCase(private val quizRepositoryFactory: QuizRepositoryFactory) :
    SingleUseCase<QuizResultEntity>() {

    private lateinit var responsesMap: Map<String, String>
    private lateinit var user: QuizUserEntity
    private lateinit var quizSession: QuizSession

    override fun buildUseCase() = quizRepositoryFactory.create(Strategy.NETWORK).run {
        var responses = mutableListOf<QuizQuestionResponseData>()
        responsesMap.forEach {
            val response = QuizQuestionResponseData(it.key, listOf(it.value))
            responses.add(response)
        }
        val quizQuestionsRequestData = QuizQuestionsRequestData(
            QuizQuestionsRequestData.COVID_QUIZ_ID, responses
        )
        sendQuizQuestions(
            user.prefixPhone.plus(user.phone),
            user.isHealthProf,
            user.isSecurityProf,
            user.isSpecialProf,
            quizQuestionsRequestData,
            quizSession
        ).map {
            QuizResultMapper.convert(it)
        }
    }

    fun params(user: QuizUserEntity, responsesMap: Map<String, String>, quizSession: QuizSession) =
        this.apply {
            this@SendQuizResponsesUseCase.user = user
            this@SendQuizResponsesUseCase.responsesMap = responsesMap
            this@SendQuizResponsesUseCase.quizSession = quizSession
        }
}
