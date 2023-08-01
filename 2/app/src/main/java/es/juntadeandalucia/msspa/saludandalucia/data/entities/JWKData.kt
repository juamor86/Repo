package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class JWKData(
    var id: String,
    var clavePublica: String,
    var pais: String,
    var fechaInclusion: String,
    var tipo: String,
    var certificado: String,
    var kid: String
)
