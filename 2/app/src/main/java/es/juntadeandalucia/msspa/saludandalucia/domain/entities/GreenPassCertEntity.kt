package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.graphics.Bitmap

data class GreenPassCertEntity(
    val display: String = "",
    val qr: String = "",
    val userName: String = "",
    val userId: String = "",
    val certificateDate: String = "",
    // TODO CHANGE THIS VALUES IF NEEDY WHEN SERVICE IS UP
    val userBirthdate: String = "",
    val positiveRecovery: String? = "",
    val recoveryDate: String? = "",
    val negativeTestDate: String? = "",
    val testType: String? = "",
    val vaccinationDate: String? = "",
    val totalVaccines: String? = "",
    val vaccineNumber: String? = "",
    val vaccineName: String? = "",
    val userSurname: String? = "",
    val jwt: String? = ""
) {

    val hasCert: Boolean
        get() = certificateDate.isNotEmpty()

    lateinit var qrBitmap: Bitmap
    lateinit var nuhsa: String
}
