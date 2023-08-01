package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationError
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import es.juntadeandalucia.msspa.authentication.domain.usecases.*
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.other.TotpGenerator
import timber.log.Timber

class QRVerificationPresenter(
    private val checkPinUseCase: CheckPinUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val removePinUseCase: RemovePinUseCase,
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val invalidateTokenUseCase: InvalidateTokenUseCase
) :
    BasePresenter<QRVerificationContract.View>(), QRVerificationContract.Presenter {

    private lateinit var msspaAuthenticationEntity: MsspaAuthenticationEntity
    private lateinit var msspaAuthenticationConfig: MsspaAuthenticationConfig
    private var loggingAttempts = 0

    override fun onCreate(
        authEntity: MsspaAuthenticationEntity,
        authConfig: MsspaAuthenticationConfig
    ) {
        msspaAuthenticationEntity = authEntity
        msspaAuthenticationConfig = authConfig
        view.setupView()
        checkAuthForEncryption()
    }

    private fun checkValidAccessToken(){
        view.apply {
            showLoading()
            validateTokenUseCase.params(msspaAuthenticationEntity.accessToken).execute(
                onSuccess = { valid ->
                    if(valid){
                        Timber.d("Token is valid")
                        hideLoading()
                        onLoginSuccess()
                    }else{
                        Timber.d("Token is NOT valid")
                        performRefreshToken(msspaAuthenticationEntity, msspaAuthenticationConfig)
                    }
                },
                onError = {
                    hideLoading()
                    Timber.e(it)
                }
            )
        }
    }

    private fun performRefreshToken(authEntity: MsspaAuthenticationEntity, authConfig: MsspaAuthenticationConfig){
        refreshTokenUseCase.params(
            refreshToken = authEntity.refreshToken!!,
            clientId = authConfig.clientId,
            totp = TotpGenerator.generateGoogleAuthenticate(authEntity.totpSecretKey!!)
        ).execute(
            onSuccess = {
                Timber.d("Refresh token OK")
                view.hideLoading()
                this.msspaAuthenticationEntity = it
                this.msspaAuthenticationEntity.totpSecretKey = authEntity.totpSecretKey!!
                onLoginSuccess()
            },
            onError = {
                view.apply {
                    Timber.d("Refresh token ko")
                    hideLoading()
                    Timber.e(it)
                    invalidateToken(authEntity)
                    setResultError(MsspaAuthenticationError.SESSION_EXPIRED)
                }
            }
        )
    }

    override fun onLoginSuccess() {
        view.setResultSuccess(msspaAuthenticationEntity)
    }

    override fun onPinFailed() {
        view.setResultError(MsspaAuthenticationError.QR_LOGIN_ERROR)
    }

    override fun onSendPinClicked(pin: String, cryptoManager: CrytographyManager) {
        checkPinUseCase.apply {
            params(pin)
            execute(
                onSuccess = { isPinValid ->
                    if (isPinValid) {
                        checkValidAccessToken()
                    } else {
                        loggingAttempts += 1
                        if (loggingAttempts < MsspaAuthConsts.MAX_PIN_ATTEMPTS) {
                            view.showPinError()
                        } else {
                            invalidateToken(msspaAuthenticationEntity)
                            view.setResultWarning(MsspaAuthenticationWarning.PIN_MAX_ATTEMPTS)
                        }
                    }
                },
                onError = {
                    Timber.e(it)
                }
            )
        }
    }

    private fun checkAuthForEncryption() {
        view.apply {
            authenticateForEncryption(
                onSuccess = { _, _ ->
                    checkValidAccessToken()
                },
                onError = {
                    Timber.e(it)
                },
                onErrorInt = {
                    Timber.e("Authenticate encryption an error occurred.")
                },
                encrypt = false,
                keyString = ApiConstants.KeyNameCipher.KEY_SAVED_PIN,
                isNeedCipher = false
            )
        }
    }

    override fun onCancelClicked() {
        view.showCancelLoginPopUp()
    }

    override fun cleanLoginQRandFinish() {
        removePinUseCase.execute(
            onComplete = {
                Timber.d("PIN delete successfully")
            },
            onError = {
                Timber.e(it)
            }
        )
        view.setResultError(MsspaAuthenticationError.CANCEL_LOGIN)
    }

    private fun invalidateToken(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        msspaAuthenticationEntity.authorizationToken?.let { authorizationToken ->
            invalidateTokenUseCase.params(msspaAuthenticationEntity.accessToken, authorizationToken)
                .execute(onComplete = {}, onError = { Timber.e(it) })
        }
    }

    override fun unsubscribe() {
        checkPinUseCase.clear()
        refreshTokenUseCase.clear()
        removePinUseCase.clear()
        validateTokenUseCase.clear()
        invalidateTokenUseCase.clear()
    }
}