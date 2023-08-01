package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class AppData(
    @SerializedName("id")
    var appId: String,
    var type: String,
    var name: String,
    var description: String,
    var alt: String,
    var category: String,
    var icon: String,
    var link: String,
    var version: String,
    var images: List<String>?
)
