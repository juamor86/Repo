package es.juntadeandalucia.msspa.authentication.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthorizeEntity(
    val sessionId: String,
    val sessionData: String
) : Parcelable
