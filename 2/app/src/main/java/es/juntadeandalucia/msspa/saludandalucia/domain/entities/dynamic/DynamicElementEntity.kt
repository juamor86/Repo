package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicElementEntity(
    val accessLevel: String = "",
    val background: String = "",
    val icon: IconEntity = IconEntity(),
    val id: Int = 0,
    val navigation: NavigationEntity = NavigationEntity(),
    val subtitle: SubtitleEntity = SubtitleEntity(),
    val title: TitleEntity = TitleEntity(),
    val type: String = ""
) : Parcelable
