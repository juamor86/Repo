package es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring

data class MonitoringData(
    val entry: List<Entry>,
    val id: String,
    val link: List<Link>,
    val meta: Meta,
    val resourceType: String,
    val total: Int,
    val type: String
) {
    data class Entry(
        val resource: Resource
    ) {
        data class Resource(
            val code: List<Code>,
            val extension: List<Extension>,
            val id: String,
            val identifier: List<Identifier>,
            val meta: Meta,
            val name: String,
            val resourceType: String,
            val status: String
        ) {
            data class Code(
                val code: String,
                val display: String,
                val system: String
            )

            data class Extension(
                val url: String,
                val valueReference: ValueReference
            ) {
                data class ValueReference(
                    val display: String,
                    val id: String,
                    val type: String
                )
            }

            data class Identifier(
                val value: String
            )

            data class Meta(
                val lastUpdated: String
            )
        }
    }

    data class Link(
        val relation: String,
        val url: String
    )

    data class Meta(
        val lastUpdated: String
    )
}
