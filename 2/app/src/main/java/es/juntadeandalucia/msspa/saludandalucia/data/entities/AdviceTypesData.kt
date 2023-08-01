package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class AdviceTypesData (
        val id: String,
        val resourceType: String,
        val meta: AdviceTypeMetaData,
        val type: String,
        val total: Long,
        val link: List<AdviceTypeLinkData>,
        val entry: List<AdviceTypeEntryData>
)

data class AdviceTypeEntryData (
        val resource: AdviceTypeResourceData
)

data class AdviceTypeResourceData (
        val id: String,
        val resourceType: String,
        val text: String,
        val status: String,
        val reason: String,
        val criteria: String,
        val channel: AdviceTypeChannelData
)

data class AdviceTypeChannelData (
        val type: String
)

data class AdviceTypeLinkData (
        val relation: String,
        val url: String
)

data class AdviceTypeMetaData (
        val lastUpdated: String
)
