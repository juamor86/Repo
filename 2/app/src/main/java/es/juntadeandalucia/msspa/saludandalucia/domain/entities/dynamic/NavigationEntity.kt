package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavigationEntity(
    val target: String = "",
    val type: String = "",
    val level: String? = "",
    var title: String = "",
    var bundle: Bundle? = null,
    var navigateAfterLogin: Boolean = true
) : Parcelable
