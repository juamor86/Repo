package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class MeasurementsData(
    val entry: List<Entry>,
    val id: String,
    val link: List<Link>,
    val meta: Meta,
    val resourceType: String,
    val total: Int,
    val type: List<String>
) {
    data class Entry(
        @SerializedName("resource")
        val resources: List<Resource>
    ) {
        data class Resource(
            val author: Author,
            val authored: String,
            val extension: List<Extension>,
            val id: String,
            val item: List<Item>,
            val meta: Meta,
            val questionnaire: String,
            val resourceType: String,
            val status: String,
            val subject: Subject
        ) {
            data class Author(
                val display: String,
                @SerializedName("extension")
                val authorExtension: List<AuthorExtension>,
                val type: String
            ) {
                data class AuthorExtension(
                    val url: String
                )
            }

            data class Extension(
                val url: String,
                val valueDecimal: Double,
                val valueString: String
            )

            data class Item(
                val answer: List<Answer>,
                @SerializedName("extension")
                val itemExtension: List<ItemExtension>,
                @SerializedName("item")
                val subItem: List<SubItem?>,
                val linkId: String,
                val text: String
            ) {
                data class Answer(
                    val valueBoolean: Boolean
                )

                data class SubItem(
                    val answer: List<Answer>,
                    @SerializedName("extension")
                    val subItemExtension: List<ItemExtension>,
                    val linkId: String,
                    val text: String
                ) {
                    data class Answer(
                        val valueInteger: Int?,
                        val valueDecimal: Double?,
                        val valueTime: String?
                    )
                }
            }

            data class Meta(
                val lastUpdated: String
            )

            data class Subject(
                val id: String,
                val reference: String,
                val type: String
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

data class ItemExtension(
    val url: String,
    val valueCode: String?,
    val valueCoding: ValueCoding?
) {
    data class ValueCoding(
        val code: String,
        val display: String,
        val system: String
    )
}
