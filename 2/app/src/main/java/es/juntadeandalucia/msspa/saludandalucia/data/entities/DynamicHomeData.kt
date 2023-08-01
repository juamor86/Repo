package es.juntadeandalucia.msspa.saludandalucia.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class DynamicHomeData(
    val meta: Meta = Meta(),
    val parameter: List<Parameter> = listOf(),
    val resourceType: String = ""
)

@Parcelize
data class Meta(
    val lastUpdated: String = ""
):Parcelable

data class Parameter(
    val children: List<Children> = listOf(),
    val id: Int = 0,
    val target: String = "",
    val title: Title = Title(),
    val type: String = ""
)

data class Children(
    val access_level: String = "",
    val background: Background = Background(),
    val icon: Icon = Icon(),
    val id: Int = 0,
    val navigation: Navigation = Navigation(),
    val subtitle: Subtitle = Subtitle(),
    val title: Title = Title(),
    val type: String = ""
)

data class Background(
    val source: String = ""
)

data class Icon(
    val source: String = ""
)

data class Navigation(
    val target: String = "",
    val type: String = ""
)

data class Subtitle(
    val color: String = "",
    val text: String = ""
)

data class Title(
    val alt: String = "",
    val color: String = "",
    val text: String = ""
)
