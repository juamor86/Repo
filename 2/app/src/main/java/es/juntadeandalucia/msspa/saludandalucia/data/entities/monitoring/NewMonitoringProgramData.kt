package es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.data.entities.Meta
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import kotlinx.android.parcel.Parcelize

data class NewMonitoringProgramData(
    val code: List<Code> = listOf(),
    val date: String = "",
    val extension: List<ExtensionData> = listOf(),
    val id: String = "",
    val identifier: List<Identifier> = listOf(),
    val item: List<ItemData> = listOf(),
    val meta: Meta = Meta(),
    val name: String = "",
    val purpose: String = "",
    val resourceType: String = "",
    val status: String = "",
    val title: String = ""
)

@Parcelize
data class Code(
    val code: String = "",
    val display: String = "",
    val system: String = ""
):Parcelable

@Parcelize
data class Identifier(
    val value: String = ""
): Parcelable
