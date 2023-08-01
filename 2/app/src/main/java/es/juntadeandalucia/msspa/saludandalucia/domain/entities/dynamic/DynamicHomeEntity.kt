package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Title

data class DynamicHomeEntity(
    val meta: MetaEntity = MetaEntity(),
    val layouts: List<LayoutEntity> = listOf(),
    val resourceType: String = ""
)

data class MetaEntity(
    val lastUpdated: String = ""
)

data class LayoutEntity(
    val children: List<DynamicElementEntity> = listOf(),
    val id: Int = 0,
    val target: String = "",
    val title: Title = Title(),
    val type: String = ""
)
