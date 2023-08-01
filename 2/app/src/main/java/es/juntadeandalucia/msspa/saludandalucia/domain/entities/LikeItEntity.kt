package es.juntadeandalucia.msspa.saludandalucia.domain.entities


data class LikeItEntity(val lastUpdate: String, val events: Map<String,EventEntity>, val actions: Map<String,ActionEntity>, val dialog: Map<String,DialogEntity>)

