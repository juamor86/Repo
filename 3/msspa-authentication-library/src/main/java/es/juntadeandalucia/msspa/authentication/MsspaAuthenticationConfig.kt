package es.juntadeandalucia.msspa.authentication

import android.os.Parcelable
import androidx.annotation.LayoutRes
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MsspaAuthenticationConfig(
    val environment: Environment = Environment.PRODUCTION,
    val clientId: String,
    val apiKey: String,
    val appKey: String,
    val version: String,
    val idSo: String,
    val logoToolbar: Int? = null,
    val colorToolbar: Int? = null,
    @LayoutRes val errorLayout: Int? = null,
    val errors: List<MsspaAuthenticationError.MsspaAuthenticationErrorResource> = emptyList(),
    @LayoutRes val warningLayout: Int? = null,
    val warnings: List<MsspaAuthenticationWarning.MsspaAuthenticationWarningResource> = emptyList()
) : Parcelable {

    companion object {
        // There is a typo in the redirect URI, so we need to define this constant
        private const val AUTHENTICATION_REDIRECT = "autentication"
    }

    enum class Environment(val url: String) {
        PREPRODUCTION(MsspaAuthConsts.API_BASE_HOST_PRE),
        PRODUCTION(MsspaAuthConsts.API_BASE_HOST_PRO)
    }

    @IgnoredOnParcel
    val redirectURI = "${appKey}://${AUTHENTICATION_REDIRECT}"

}
