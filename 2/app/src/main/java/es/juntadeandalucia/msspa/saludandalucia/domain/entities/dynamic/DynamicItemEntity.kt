package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynamicItemEntity(
    var children: MutableList<DynamicItemEntity> = mutableListOf(),
    val icon: IconEntity = IconEntity(),
    val id: Int = 0,
    val navigation: NavigationEntity? = NavigationEntity(),
    val title: TitleEntity? = TitleEntity(),
    var isParent: Boolean = false
) : Parcelable {

    override fun toString(): String {
        return title?.text ?: ""
    }
}
