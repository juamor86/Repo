package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestVerificationCodeEntity(val idVerification: String, val phoneNumber: String) :
    Parcelable
