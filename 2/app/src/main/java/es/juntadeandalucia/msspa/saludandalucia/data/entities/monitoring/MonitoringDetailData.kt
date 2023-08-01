package es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring

data class MonitoringDetailData(
    var title: String,
    val detailText: String,
    val date: String,
    val hour: String,
    val questions: List<MonitoringQuestionAnwseredData>
)
