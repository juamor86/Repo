package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class QuizData(
    val available: Boolean,
    @SerializedName("next_try_millis")
    val nextTry: Long,
    @SerializedName("next_try_date")
    val nextTryDate: String,
    val questions: List<QuizQuestionData>,
    val appointment: AppointmentData?
)
