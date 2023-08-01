package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class BeneficiaryListData(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("meta")
    val meta: Meta = Meta(),
    @SerializedName("resourceType")
    val resourceType: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("entry")
    val entry: List<Entry> = listOf()
)

data class Entry(
    @SerializedName("resource")
    val resource: Resource = Resource()
)

data class Resource(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("meta")
    val meta: Meta = Meta(),
    @SerializedName("resourceType")
    val resourceType: String = "",
    @SerializedName("identifier")
    val identifier: List<Identifier> = listOf(),
    @SerializedName("patient")
    val patient: Patient = Patient(),
    @SerializedName("relationship")
    val relationship: List<Relationship> = listOf(),
    @SerializedName("name")
    val name: List<Name> = listOf()
)

data class Identifier(
    @SerializedName("type")
    val type: Type = Type(),
    @SerializedName("value")
    val value: String = ""
)

data class Type(
    @SerializedName("text")
    val text: String = ""
)

data class Name(
    @SerializedName("extension")
    val extension: List<Extension> = listOf(),
    @SerializedName("text")
    val text: String = "",
    @SerializedName("given")
    val given: List<String> = listOf()
)

data class Extension(
    @SerializedName("url")
    val url: String = "",
    @SerializedName("valueString")
    val valueString: String = ""
)

data class Patient(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("display")
    val display: String = ""
)

data class Relationship(
    @SerializedName("coding")
    val coding: List<Coding> = listOf()
)

data class Coding(
    @SerializedName("system")
    val system: String = "",
    @SerializedName("version")
    val version: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("display")
    val display: String = ""
)
