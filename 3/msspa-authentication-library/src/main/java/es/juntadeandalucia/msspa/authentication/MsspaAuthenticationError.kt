package es.juntadeandalucia.msspa.authentication

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MsspaAuthenticationError(
    @StringRes internal var errorTitle: Int? = R.string.msspa_auth_dialog_error_title,
    @StringRes internal var errorMessage: Int? = R.string.msspa_auth_dialog_error_message
) : Parcelable {
    GENERIC_ERROR,
    LOADING_WEB,
    WRONG_CONFIG,
    NETWORK,
    MAX_ATTEMPTS,
    INVALID_PIN_SMS_RECEIVED,
    QR_LOGIN_ERROR,
    NO_CERTIFICATE,
    DNIE_CERT_EXPIRED,
    DNIE_GENERIC_ERROR,
    INVALID_CERTIFICATE,
    INVALID_REQUEST,
    RATE_LIMIT_EXCEEDED,
    SEND_NOTIFICATION_ERROR,
    CANCEL_LOGIN,
    ABORT_INIT_LOGIN,
    ERROR_TOKEN_VALIDATE,
    ERROR_TOKEN_INVALIDATE,
    ERROR_REFRESH_TOKEN,
    SESSION_EXPIRED;

    @Parcelize
    class MsspaAuthenticationErrorResource(
        val error: MsspaAuthenticationError,
        @StringRes private val errorTitle: Int? = null,
        @StringRes private val errorMessage: Int? = null
    ) : Parcelable {
        init {
            error.errorTitle = errorTitle
            error.errorMessage = errorMessage
        }
    }
}
