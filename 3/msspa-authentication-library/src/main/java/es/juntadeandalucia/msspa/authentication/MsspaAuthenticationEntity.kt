package es.juntadeandalucia.msspa.authentication

import android.os.Parcelable
import com.auth0.android.jwt.JWT
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MsspaAuthenticationEntity(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: Int?,
    val scope: MsspaAuthenticationManager.Scope,
    val refreshToken: String? = null,
    var totpSecretKey: String? = null,
    var msspaAuthenticationUser: MsspaAuthenticationUserEntity? = null,
    var authorizeEntity: AuthorizeEntity? = null
) : Parcelable {
    companion object {
        private const val DATA_SEPARATOR = " : "
    }

    init {
        if (accessToken.isNotBlank()) {
            val data = JWT(accessToken).subject?.split(DATA_SEPARATOR)
            data?.apply {
                if (size == 3) {
                    if (msspaAuthenticationUser == null) {
                        msspaAuthenticationUser = MsspaAuthenticationUserEntity()
                    }
                    msspaAuthenticationUser?.apply {
                        nuhsa = data[1]
                        name = data[2]
                    }
                }
            }
        }
    }

    val authorizationToken: String?
        get() {
            return if (tokenType.isNotBlank() && accessToken.isNotBlank()) {
                "$tokenType $accessToken"
            } else null
        }

    fun isGrantedAccessToScope(scope: String): Boolean {
        val destScope = MsspaAuthenticationManager.Scope.getScope(scope)
        return this.scope.ordinal >= destScope.ordinal
    }

}
