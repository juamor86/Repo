package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class QuizQuestionResponseData(@SerializedName("question_id") val questionId: String, val responses: List<String>)
