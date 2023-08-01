package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class MeasureHelperData(
    val code: List<Code>,
    val date: String,
    val extension: List<Extension>,
    val id: String,
    val identifier: List<Identifier>,
    val item: List<Item>,
    val meta: Meta,
    val name: String,
    val purpose: String,
    val resourceType: String,
    val status: String,
    val title: String
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

    data class Item(
        val extension: List<Extension>,
        val linkId: String,
        val required: Boolean,
        val text: String,
        val type: String
    ) {
        data class Extension(
            val url: String,
            val valueCoding: ValueCoding
        ) {
            data class ValueCoding(
                val code: String,
                val display: String,
                val system: String
            )
        }
    }

    data class Meta(
        val lastUpdated: String
    )
}
