package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class GreenPassCertData(
    val content: List<Content>,
    val id: String,
    val relatesTo: List<RelatesTo>,
    val resourceType: String,
    val status: String,
    val type: Type
) {

    data class Content(
        val attachment: Attachment,
        val format: Format
    ) {
        data class Attachment(
            val contentType: String,
            val data: String
        )

        data class Format(
            val code: String
        )
    }

    data class RelatesTo(
        val code: String,
        val target: Target
    ) {
        data class Target(
            val display: String,
            val reference: String,
            val type: String
        )
    }

    data class Type(
        val coding: List<Coding>
    ) {
        data class Coding(
            val code: String,
            val display: String
        )
    }
}
