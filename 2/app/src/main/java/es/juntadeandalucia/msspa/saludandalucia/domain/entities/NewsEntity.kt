package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsEntity(
    var title: String?,
    var subtitle: String?,
    var description: String?,
    var url: String?,
    var imgHeader: String?,
    var operationMode:String?
) : Parcelable
