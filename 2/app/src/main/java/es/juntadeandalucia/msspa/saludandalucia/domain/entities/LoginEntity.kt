package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import com.auth0.android.jwt.JWT
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginEntity(
    val token: String?,
    val tokenType: String?,
    val expires: String?,
    val scope: String?,
    val active: Boolean?
) : Parcelable {

    companion object {
        private const val DATA_SEPARATOR = " : "
    }

    lateinit var userEntity: QuizUserEntity

    init {
        token?.let {
            val data = JWT(token).subject?.split(DATA_SEPARATOR)
            data?.apply {
                if (size == 3) {
                    userEntity =
                        QuizUserEntity(identification = data[0], nuhsa = data[1], name = data[2])
                }
            }
        }
    }
}
