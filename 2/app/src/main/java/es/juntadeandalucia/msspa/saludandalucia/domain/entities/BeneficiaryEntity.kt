package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeneficiaryEntity(
    val nuhsa: String,
    val fullName: String,
    val token: String
) : Parcelable
