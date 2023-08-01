package es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements

data class MeasureHelperEntity(
    val groupName: String = "",
    val range: String = "",
    var helpText: String = ""
)
