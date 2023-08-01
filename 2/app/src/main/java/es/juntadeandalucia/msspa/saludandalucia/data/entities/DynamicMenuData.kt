package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class DynamicMenuData(
    val resourceType: String,
    val meta: MenuMeta,
    val parameter: List<DynamicMenuDataItem>
)

data class MenuMeta(
    val lastUpdated: String = ""
)

data class DynamicMenuDataItem(
    val area_desc: String = "",
    val area_id: String = "",
    val children: List<ChildrenData> = listOf()
)

data class ChildrenData(
    val children: List<ChildrenData>? = listOf(),
    val icon: IconData? = IconData(),
    val id: Int = 0,
    val navigation: NavigationData? = NavigationData(),
    val title: TitleData? = TitleData(),
    val access_level: String
)

data class IconData(
    val source: String = ""
)

data class NavigationData(
    val target: String = "",
    val type: String = ""
)

data class TitleData(
    val alt: String = "",
    val text: String = ""
)
