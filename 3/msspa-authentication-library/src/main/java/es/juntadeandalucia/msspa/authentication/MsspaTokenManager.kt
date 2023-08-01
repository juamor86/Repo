package es.juntadeandalucia.msspa.authentication

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import es.juntadeandalucia.msspa.authentication.domain.usecases.InvalidateTokenUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.RefreshTokenUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.ValidateTokenUseCase
import es.juntadeandalucia.msspa.authentication.utils.other.TotpGenerator
import timber.log.Timber
import javax.inject.Inject

class MsspaTokenManager @Inject constructor(
    val validateTokenUseCase: ValidateTokenUseCase,
    val invalidateTokenUseCase: InvalidateTokenUseCase,
    val refreshTokenUseCase: RefreshTokenUseCase
) {

    fun validateToken(msspaAuthenticationEntity: MsspaAuthenticationEntity, context: Context) {
        validateTokenUseCase.params(msspaAuthenticationEntity.accessToken).execute(
            onSuccess = { isValid ->
                launchSuccessValidate(context, isValid)
            },
            onError = {
                launchError(context, MsspaAuthenticationError.ERROR_TOKEN_VALIDATE)
                Timber.e(it)
            }
        )
    }

    fun invalidateToken(msspaAuthenticationEntity: MsspaAuthenticationEntity, context: Context) {
        msspaAuthenticationEntity.authorizationToken?.let { authorizationToken->
            invalidateTokenUseCase
                .params(msspaAuthenticationEntity.accessToken, authorizationToken)
                .execute(
                    onComplete = {
                        launchSuccessInValidate(context)
                    },
                    onError = {
                        Timber.e(it)
                        launchError(context, MsspaAuthenticationError.ERROR_TOKEN_INVALIDATE)
                    }
                )
        }
    }

    fun refreshToken(
        authenticationEntity: MsspaAuthenticationEntity,
        authenticationConfig: MsspaAuthenticationConfig,
        context: Context
    ) {
        refreshTokenUseCase.params(
            refreshToken = authenticationEntity.refreshToken!!,
            clientId = authenticationConfig.clientId,
            totp = TotpGenerator.generateGoogleAuthenticate(authenticationEntity.totpSecretKey!!)
        ).execute(
            onSuccess = { msspaAuthenticationEntity ->
                launchSuccessRefreshToken(context, msspaAuthenticationEntity)
            },
            onError = {
                Timber.e(it)
                launchError(context, MsspaAuthenticationError.ERROR_REFRESH_TOKEN)
            }
        )
    }

    private fun launchError(context: Context, errorType: MsspaAuthenticationError) {
        val intent = Intent().apply {
            action = MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT_ERROR
            )
            putExtra(MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_ERROR_ARG, errorType as Parcelable)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun launchSuccessValidate(context: Context, isValid: Boolean) {
        val intent = Intent().apply {
            action = MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT_VALIDATE_TOKEN
            )
            putExtra(MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_VALIDATE_TOKEN_ARG, isValid)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun launchSuccessInValidate(context: Context) {
        val intent = Intent().apply {
            action = MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT_INVALIDATE_TOKEN
            )
            putExtra(MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_INVALIDATE_TOKEN_ARG, true)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun launchSuccessRefreshToken(
        context: Context,
        msspaAuthenticationEntity: MsspaAuthenticationEntity
    ) {
        val intent = Intent().apply {
            action = MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT_REFRESH_TOKEN
            )
            putExtra(MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_REFRESH_TOKEN_ARG, msspaAuthenticationEntity as Parcelable)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}