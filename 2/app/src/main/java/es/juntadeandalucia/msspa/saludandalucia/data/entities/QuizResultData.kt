package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class QuizResultData(
    @SerializedName("result_type") val resultType: String,
    val result: String,
    @SerializedName("next_try_millis") val nextTryMillis: Long,
    val appointment: AppointmentData?
) {
    companion object {
        const val POSITIVE_RESULT = "positivo"
        const val NEGATIVE_RESULT = "negativo"
    }
}
