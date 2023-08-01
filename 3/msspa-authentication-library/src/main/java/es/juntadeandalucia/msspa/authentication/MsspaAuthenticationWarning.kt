package es.juntadeandalucia.msspa.authentication

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MsspaAuthenticationWarning(
    @StringRes internal var warningTitle: Int? = R.string.msspa_auth_dialog_info_title,
    @StringRes internal var warningMessage: Int? = R.string.msspa_auth_dialog_info_message
) : Parcelable {
    GENERIC_WARNING,
    INVALID_BDU_DATA,
    INVALID_BDU_PHONE,
    PROTECTED_USER,
    QR_LOGIN_NOT_FOUND_WARNING,
    QR_LOGIN_EXPIRED_WARNING,
    PIN_MAX_ATTEMPTS,
    NOT_PRIVATE_HEALTH_CARE,
    OTHER_AUTH_METHODS;

    @Parcelize
    class MsspaAuthenticationWarningResource(
        val warning: MsspaAuthenticationWarning,
        @StringRes private val warningTitle: Int? = null,
        @StringRes private val warningMessage: Int? = null
    ) : Parcelable {
        init {
            warning.warningTitle = warningTitle
            warning.warningMessage = warningMessage
        }
    }

}
