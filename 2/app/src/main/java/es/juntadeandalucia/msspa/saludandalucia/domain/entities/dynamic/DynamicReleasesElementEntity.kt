package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicReleasesElementEntity(
    val id: Int = 0,
    val title: TitleEntity = TitleEntity(),
) : Parcelable
