package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionsRequestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizResultData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.QuizRepository
import io.reactivex.Single

class QuizRepositoryMockImpl() : QuizRepository {

    override fun getQuizQuestions(quizSession: QuizSession): Single<QuizData> =
        Single.just(
            QuizData(
                true,
                System.currentTimeMillis() + 60 * 60 * 1000,
                "",
                emptyList(),
            null
            )

        )

    override fun sendQuizQuestions(
        phoneNumber: String,
        isHealthProf: Boolean,
        isSecurityProf: Boolean,
        isSpecialProf: Boolean,
        quizQuestionsRequestData: QuizQuestionsRequestData,
        quizSession: QuizSession
    ): Single<QuizResultData> = Single.just(
        QuizResultData(
            "positivo",
            "Según los datos que has aportado, pareces no tener síntomas o no son suficientes para determinar que tienes coronavirus. En cualquier caso el coronavirus se presenta con síntomas leves en la mayoría de ocasiones. Si los síntomas que tienes persisten o ves que no mejoras, vuelve a autoevaluarte dentro de 12 horas, y si en cualquier caso tu estado empeora, contacta telefónicamente con tu centro de salud y en caso de emergencia al XXX XXX XXX. Recuerda que los servicios médicos y de atención telefónica deben ser usados únicamente por pacientes que presenten gravedad. ¡Usémoslos con responsabilidad!",
            123123123, null
        )
    )
}
