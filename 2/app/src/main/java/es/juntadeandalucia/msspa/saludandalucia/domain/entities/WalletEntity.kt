package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletEntity(
    var id: String,
    var type: WalletType,
    var name: String,
    var surname: String,
    var qr: String,
    var jwt: String
) : Parcelable

