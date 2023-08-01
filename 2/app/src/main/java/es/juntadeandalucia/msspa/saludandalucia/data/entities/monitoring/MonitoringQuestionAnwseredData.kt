package es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring

data class MonitoringQuestionAnwseredData(
    val questionId: String,
    val question: String,
    val type: String,
    val mandatory: Boolean,
    val options: List<String> = emptyList(),
    val answerBoolean: Boolean = false,
    val answerText: String = ""
) {
    companion object {
        const val BOOLEAN_TYPE = "BOOLEAN"
        const val DECIMAL_TYPE = "DECIMAL"
        const val TEXT_TYPE = "TEXT"
        const val OPTIONS_TYPE = "OPTIONS"
        const val SINGLE_OPTIONS_TYPE = "SINGLE"
        const val MULTIPLE_OPTIONS_TYPE = "MULTIPLE"
    }
}
