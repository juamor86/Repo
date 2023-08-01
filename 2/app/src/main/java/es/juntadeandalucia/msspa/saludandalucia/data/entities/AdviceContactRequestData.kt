package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class AdviceContactRequestData(
    @SerializedName("system") val system: String = "phone",
    @SerializedName("value") val value: String,
    @SerializedName("use") val use: String = "mobile"
)