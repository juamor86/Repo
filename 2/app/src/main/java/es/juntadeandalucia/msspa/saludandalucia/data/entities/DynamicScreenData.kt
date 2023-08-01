package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class DynamicScreenData(
    val meta: Meta,
    val parameter: List<Parameter>,
    val resourceType: String
) {
    data class Meta(
        val lastUpdated: String
    )

    data class Parameter(
        val background: Background,
        val children: List<Children>,
        val header: Header?,
        val id: String,
        val title: Title?
    ) {
        data class Background(
            val source: String
        )

        data class Children(
            val access_level: String?,
            val icon: Icon?,
            val id: Int,
            val navigation: Navigation,
            val title: Title
        ) {
            data class Icon(
                val source: String
            )

            data class Navigation(
                val target: String,
                val type: String
            )

            data class Title(
                val alt: String,
                val text: String
            )
        }

        data class Header(
            val source: String
        )

        data class Title(
            val alt: String,
            val color: String,
            val text: String
        )
    }
}
