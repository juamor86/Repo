package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionsRequestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizResultData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.QuizRepository
import io.reactivex.Single

class QuizRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    QuizRepository {

    override fun getQuizQuestions(quizSession: QuizSession) =
        msspaApi.getQuizQuestions(quizSession.authorizationToken!!)

    override fun sendQuizQuestions(
        phoneNumber: String,
        isHealthProf: Boolean,
        isSecurityProf: Boolean,
        isSpecialProf: Boolean,
        quizQuestionsRequestData: QuizQuestionsRequestData,
        quizSession: QuizSession
    ): Single<QuizResultData> {
        val tipProfessional =
            if (isHealthProf) "S" else "N".plus(",").plus(if (isSecurityProf) "S" else "N")
                .plus(",")
                .plus(if (isSpecialProf) "S" else "N")
        return msspaApi.sendQuizQuestions(
            quizQuestionsRequestData,
            phoneNumber,
            tipProfessional,
            quizSession.authorizationToken
        )
    }
}
