package es.juntadeandalucia.msspa.authentication.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MsspaAuthenticationUserEntity(
    var name: String = "",
    var nuss: String = "",
    var nuhsa: String = "",
    var idType: KeyValueEntity = KeyValueEntity("", ""),
    var identification: String = "",
    var birthDate: String = "",
    var phone: String = ""
) : Parcelable {
    companion object {
        const val ID_TYPE_NUSS_NUHSA = "0"
        const val ID_TYPE_DNI = "1"
        const val ID_TYPE_NIE = "6"
        private const val HEALTH_PROF_TYPE_NO_KEY = "NO"
    }

    val prettyName: String
        get() {
            val split = name.split(",")
            return if (split.size == 1)
                split[0].trim()
            else
                "${split[1].trim()} ${split[0].trim()}"
        }

    val initials: String
        get() {
            val split = name.split(",")
            return if (split.size == 1)
                "${split[0].trim().first()}"
            else
                "${split[1].trim().first()}${split[0].trim().first()}"
        }
}
