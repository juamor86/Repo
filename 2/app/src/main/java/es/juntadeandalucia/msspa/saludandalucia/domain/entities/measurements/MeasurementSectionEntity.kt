package es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements

data class MeasurementSectionEntity(
    var title: String = "",
    var values: MutableList<MeasurementValueEntity> = mutableListOf(),
    var headers: MutableList<String> = mutableListOf(),
    var range: String?,
    var helpText: String?,
    var currentPage: Int,
    var lastPage: Int

)

data class MeasurementValueEntity(
    var values: List<String>,
    var units: List<String>,
    var date: String,
    var hour: String,
    var dateLong: Long
)
