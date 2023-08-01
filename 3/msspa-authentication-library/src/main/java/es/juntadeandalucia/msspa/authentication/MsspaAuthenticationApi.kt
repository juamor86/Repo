package es.juntadeandalucia.msspa.authentication

import android.content.Context
import es.juntadeandalucia.msspa.authentication.domain.usecases.RefreshTokenUseCase
import es.juntadeandalucia.msspa.authentication.utils.BiometricUtils
import es.juntadeandalucia.msspa.authentication.utils.other.TotpGenerator
import timber.log.Timber
import javax.crypto.Cipher
import javax.inject.Inject

class MsspaAuthenticationApi(val context: Context, val config: MsspaAuthenticationConfig) {

    @Inject
    lateinit var refreshTokenUseCase: RefreshTokenUseCase

    fun refreshToken(
        auth: MsspaAuthenticationEntity, onSuccess: (MsspaAuthenticationEntity) -> Unit,
        onError: (Throwable) -> Unit
    ) {


  /*      if (BiometricUtils.isBiometricOrSecured(context)) {
            view.authenticateForEncryption(
                onSuccess = { cipherEncrypt: Cipher, cipherDecrypt: Cipher ->
                    savePinUseCase
                        .params(pin, cipherEncrypt, cipherDecrypt)
                        .execute(
                            onComplete = {
                                view.setResultSuccess(authEntity)
                            },
                            onError = {
                                view.setResultSuccess(authEntity)
                                Timber.e(it)
                            }
                        )
                }, onError = {
                    view.setResultSuccess(authEntity)
                    Timber.e(it)
                })


            val totp = TotpGenerator().generate(auth.totpSecretKey)
            refreshTokenUseCase.params(
                refreshToken = auth.refreshToken!!,
                clientId = config.clientId,
                totp = totp
            ).execute(
                onSuccess = onSuccess,
                onError = onError
            )
        }
        */

    }
}