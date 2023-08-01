package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class QuizQuestionData(
    @SerializedName("id")
    val questionId: String,
    val question: String,
    val type: String,
    val mandatory: Boolean,
    val minValue: Int?,
    val maxValue: Int?,
    val maxLength: Int?,
    val cardinality: String?,
    val options: List<String>?
) {
    companion object {
        const val BOOLEAN_TYPE = "BOOLEAN"
        const val BOOLEAN_EXT_TYPE = "BOOLEAN_EXT"
        const val DECIMAL_TYPE = "DECIMAL"
        const val TEXT_TYPE = "TEXT"
        const val OPTIONS_TYPE = "OPTIONS"
    }
}
