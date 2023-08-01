package es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonitoringEntity(
    val entry: List<MonitoringEntry>
) : Parcelable {
    @Parcelize
    data class MonitoringEntry(
        val id: String,
        val title: String,
        val subtitle: String = "",
        val code: String,
        val type: String
    ) : Parcelable {
        @Parcelize
        data class Resource(
            val extension: List<Extension>,
            val identifier: String = ""
        ) : Parcelable {
            @Parcelize
            data class Extension(
                val url: String,
                val valueReference: ValueReference
            ) : Parcelable {
                @Parcelize
                data class ValueReference(
                    val display: String,
                    val id: String,
                    val type: String
                ) : Parcelable
            }
        }
    }
}
