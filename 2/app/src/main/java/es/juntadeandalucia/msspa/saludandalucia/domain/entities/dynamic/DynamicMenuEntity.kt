package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

data class DynamicMenuEntity(val map: List<DynamicAreaEntity>) : ArrayList<DynamicAreaEntity>() {
    init {
        addAll(map)
    }
}

data class DynamicAreaEntity(
    val area_desc: String = "",
    val area_id: String = "",
    val children: List<DynamicItemEntity> = listOf()
) {
    override fun toString(): String {
        return area_desc
    }
}
