package es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValueCodingData(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("display")
    val display: String = "",
    @SerializedName("system")
    val system: String = "",
    @SerializedName("version")
    val version: String = ""
) : Parcelable
