package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class DynamicReleasesData(
    val meta: Meta,
    val parameter: List<Parameter>,
    val resourceType: String
) {
    data class Meta(
        val lastUpdated: String
    )

    data class Parameter(
        val id: String,
        val background: Background?,
        val header: Header?,
        val title: String,
        val titleAlt: String?,
        val children: List<Children>?,
        val displayCheck: DisplayCheck?
    ) {
        data class Background(
            val source: String
        )

        data class Children(
            val id: Int,
            val title: Title

        ) {
            data class Title(
                val text: String,
                val alt: String
            )
        }

        data class Header(
            val source: String
        )

        data class DisplayCheck(
            val id: String,
            val title: String
        )
    }
}
