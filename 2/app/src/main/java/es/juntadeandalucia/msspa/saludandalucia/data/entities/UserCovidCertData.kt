package es.juntadeandalucia.msspa.saludandalucia.data.entities
import com.google.gson.annotations.SerializedName

data class UserCovidCertData(
    @SerializedName("content")
    val content: List<Content> = listOf(),
    @SerializedName("relatesTo")
    val relatesTo: List<RelatesTo> = listOf(),
    @SerializedName("resourceType")
    val resourceType: String = "",
    @SerializedName("status")
    val status: String = ""
)

data class Content(
    @SerializedName("attachment")
    val attachment: Attachment = Attachment(),
    @SerializedName("format")
    val format: Format = Format()
)

data class RelatesTo(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("target")
    val target: Target = Target()
)

data class Attachment(
    @SerializedName("contentType")
    val contentType: String = "",
    @SerializedName("data")
    val data: String = ""
)

data class Format(
    @SerializedName("code")
    val code: String = ""
)

data class Target(
    @SerializedName("display")
    val display: String = "",
    @SerializedName("reference")
    val reference: String = "",
    @SerializedName("type")
    val type: String = ""
)
