package es.juntadeandalucia.msspa.saludandalucia.data.entities


data class AdviceRequestData (
    val entry: List<Entry>,
    val resourceType: String,
    val total: Int,
    val type: String
) {
    data class Entry(
        val fullUrl: String,
        val request: Request,
        val resource: Resource
    ) {
        data class Request(
            val method: String,
            val url: String
        )

        data class Resource(
            val id: String? = null,
            val channel: Channel,
            val contact: List<Contact>,
            val criteria: String,
            val extension: List<Extension>,
            val reason: String,
            val resourceType: String,
            val status: String,
            val text: String
        ) {
            data class Channel(
                val type: String
            )

            data class Contact(
                var system: String,
                val use: String,
                var value: String,
                val extension: List<Extension>? = null
            )

            data class Extension(
                val url: String,
                val valueReference: ValueReference? = null,
                val valueCode: String? = null,
            ) {
                data class ValueReference(
                    val display: String,
                    val id: String,
                    val type: String,
                    val extension: List<ValueReferenceExtension>? = null
                )
            }
        }
    }
}