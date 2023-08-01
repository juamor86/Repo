package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizUserEntity(
    var name: String = "",
    var nuhsa: String = "",
    var idType: KeyValueEntity = KeyValueEntity("", ""),
    var identification: String = "",
    var birthDate: String = "",
    var phone: String = "",
    var prefixPhone: String = "",
    var isHealthProf: Boolean = false,
    var isSecurityProf: Boolean = false,
    var isSpecialProf: Boolean = false
) : Parcelable {
    companion object {
        const val ID_TYPE_NUHSA = "0"
        const val ID_TYPE_DNI = "1"
        const val ID_TYPE_NIE = "6"
        private const val HEALTH_PROF_TYPE_NO_KEY = "NO"
    }
}
