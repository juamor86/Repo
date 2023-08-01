package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class QuizQuestionsRequestData(
    @SerializedName("id") val quizId: String,
    val responses: List<QuizQuestionResponseData>
) {
    companion object {
        const val COVID_QUIZ_ID = "covid"
    }
}
