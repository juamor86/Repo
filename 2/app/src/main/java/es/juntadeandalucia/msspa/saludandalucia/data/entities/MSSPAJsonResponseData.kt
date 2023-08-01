package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class MSSPAJsonResponseData<T>(
    var total: Int,
    var type: List<String>,
    var entry: List<T>
)
