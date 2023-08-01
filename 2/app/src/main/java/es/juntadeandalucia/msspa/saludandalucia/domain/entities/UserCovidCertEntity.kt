package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.graphics.Bitmap
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

data class UserCovidCertEntity(
    var userName: String? = null,
    var userLastName: String? = null,
    var userId: String? = null,
    var birthdate: String? = null,
    var jwt: String? = null,
) {

    val hasVaccine: Boolean
        get() {
            return !birthdate.isNullOrEmpty()
        }
    val qr: String
        get() {
            return jwt?.let {
                Consts.VACCINE_CERT_QR_TEMPLATE + it
            } ?: ""
        }
    lateinit var qrBitmap: Bitmap
    lateinit var nuhsa: String
}
