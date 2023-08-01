package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class AdviceData (
    val id: String,
    val resourceType: String,
    val meta: Meta,
    val type: String,
    val total: Long,
    val link: List<Link>,
    val entry: List<EntryAdvice>
)

data class EntryAdvice (
    val id: String,
    val resourceType: String,
    val extension: List<EntryExtension>,
    val text: String,
    val status: String,
    val contact: List<Contact>? = null,
    val reason: String,
    val criteria: String,
    val channel: Channel
)

data class Channel (
    val type: String
)

data class Contact (
    val system: String,
    val value: String,
    val use: String,
    val extension: List<ContactExtension>
)

data class ContactExtension(
    val url: String,
    val valueCode: String
)

data class EntryExtension (
    val url: String,
    val valueReference: ValueReference
)

data class ValueReference (
    val type: String,
    val id: String,
    val display: String,
    val extension: List<ValueReferenceExtension>? = null
)

data class ValueReferenceExtension (
    val url: String,
    val valueCode: String
)

data class Link (
    val relation: String,
    val url: String
)

