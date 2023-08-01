package es.juntadeandalucia.msspa.authentication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants

class MsspaAuthenticationBroadcastReceiver(private val msspaAuthenticationCallback: MsspaAuthenticationCallback) :
    BroadcastReceiver() {


    companion object {
        const val MSSPA_AUTHENTICATION_BROADCAST_ACTION = "msspaAuthentication"
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT = "msspaAuthenticationResult"
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_ERROR = -1
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_SUCCESS = 1
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_WARNING = 2
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_VALIDATE_TOKEN = 3
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_INVALIDATE_TOKEN = 4
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_REFRESH_TOKEN = 5
        const val MSSPA_AUTHENTICATION_BROADCAST_RESULT_EVENT = 6
        const val MSSPA_AUTHENTICATION_ENTITY_ARG = "msspaAuthenticationEntity"
        const val MSSPA_AUTHENTICATION_ERROR_ARG = "msspaAuthenticationError"
        const val MSSPA_AUTHENTICATION_WARNING_ARG = "msspaAuthenticationWarning"
        const val MSSPA_AUTHENTICATION_VALIDATE_TOKEN_ARG = "msspaAuthenticationValidateToken"
        const val MSSPA_AUTHENTICATION_INVALIDATE_TOKEN_ARG = "msspaAuthenticationInValidateToken"
        const val MSSPA_AUTHENTICATION_REFRESH_TOKEN_ARG = "msspaAuthenticationRefreshToken"
        const val MSSPA_AUTHENTICATION_EVENT_ARG = "msspaAuthenticationRefreshToken"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            if (action?.equals(MSSPA_AUTHENTICATION_BROADCAST_ACTION) == true) {
                val result: Int? = extras?.getInt(MSSPA_AUTHENTICATION_BROADCAST_RESULT)
                result?.run {
                    when (this) {
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_SUCCESS -> {
                            val authEntity: MsspaAuthenticationEntity? =
                                getParcelableExtra(MSSPA_AUTHENTICATION_ENTITY_ARG)
                            authEntity?.run {
                                msspaAuthenticationCallback.msspaAuthenticationOnSuccess(this)
                            }
                        }
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_ERROR -> {
                            val authError: MsspaAuthenticationError? =
                                getParcelableExtra(MSSPA_AUTHENTICATION_ERROR_ARG)
                            authError?.run {
                                msspaAuthenticationCallback.msspaAuthenticationOnError(this)
                            }
                        }
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_WARNING -> {
                            val authWarning: MsspaAuthenticationWarning? =
                                getParcelableExtra(MSSPA_AUTHENTICATION_WARNING_ARG)
                            authWarning?.run {
                                msspaAuthenticationCallback.msspaAuthenticationOnWarning(this)
                            }
                        }
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_VALIDATE_TOKEN -> {
                            val tokenValidated: Boolean = getBooleanExtra(MSSPA_AUTHENTICATION_VALIDATE_TOKEN_ARG, false)
                            msspaAuthenticationCallback.msspaValidateTokenSuccess(tokenValidated)
                        }
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_INVALIDATE_TOKEN -> {
                            msspaAuthenticationCallback.msspaInValidateTokenSuccess()
                        }
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_REFRESH_TOKEN -> {
                            val authEntity: MsspaAuthenticationEntity? =
                                getParcelableExtra(MSSPA_AUTHENTICATION_REFRESH_TOKEN_ARG)
                            authEntity?.run {
                                msspaAuthenticationCallback.msspaRefreshTokenSuccess(this)
                            }
                        }
                        MSSPA_AUTHENTICATION_BROADCAST_RESULT_EVENT -> {
                            val event: String = getStringExtra(MSSPA_AUTHENTICATION_EVENT_ARG) ?: ""
                            msspaAuthenticationCallback.msspaEventLaunch(event)
                        }
                        else -> {
                            // does nothing
                        }
                    }
                }
            }
        }
    }

    interface MsspaAuthenticationCallback {
        fun msspaAuthenticationOnSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity)
        fun msspaAuthenticationOnError(msspaAuthenticationError: MsspaAuthenticationError)
        fun msspaAuthenticationOnWarning(msspaAuthenticationWarning: MsspaAuthenticationWarning)
        fun msspaValidateTokenSuccess(isSuccess:Boolean)
        fun msspaInValidateTokenSuccess()
        fun msspaRefreshTokenSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity)
        fun msspaEventLaunch(event:String)
    }
}