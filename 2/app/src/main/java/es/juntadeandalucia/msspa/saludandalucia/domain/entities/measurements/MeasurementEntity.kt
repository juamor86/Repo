package es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements

data class MeasurementEntity(
    var title: String,
    var items: List<MeasurementItemEntity>,
    var currentPage: String,
    var nextPage: String
)

data class MeasurementItemEntity(
    var title: String = "",
    var valueInteger: Int?,
    var valueDecimal: Double?,
    var valueTime: String?,
    var valueDate: String?,
    var valueLong: Long?,
    var unit: String = ""
)
