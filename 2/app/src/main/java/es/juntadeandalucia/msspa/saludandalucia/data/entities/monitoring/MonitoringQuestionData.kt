package es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring

class MonitoringQuestionData(
    val type: String,
    val questionId: String,
    val question: String,
    val mandatory: Boolean,
    val minValue: Int = 0,
    val maxValue: Int = 0,
    val maxLength: Int = 0,
    val cardinality: String = "",
    val options: List<String> = emptyList()
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
