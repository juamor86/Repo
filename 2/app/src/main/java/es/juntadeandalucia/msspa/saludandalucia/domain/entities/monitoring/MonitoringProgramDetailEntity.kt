package es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring

data class MonitoringProgramDetailEntity(
    var title: String,
    val detailText: String,
    val date: String,
    val hour: String,
    val questions: List<MonitoringProgramQuestionAnwseredEntity>
)
