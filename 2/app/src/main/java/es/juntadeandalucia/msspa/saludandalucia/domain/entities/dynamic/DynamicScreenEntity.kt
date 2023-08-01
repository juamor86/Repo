package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicScreenEntity(
    val background: String?,
    val children: List<DynamicElementEntity>,
    val header: String?,
    val id: String,
    val title: TitleEntity?
) : Parcelable
