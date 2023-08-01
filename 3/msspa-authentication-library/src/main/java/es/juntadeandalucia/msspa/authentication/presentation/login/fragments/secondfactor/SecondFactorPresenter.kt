package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.secondfactor

import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.AuthorizeUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.LoginPersonalDataReinforcedUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.LoginPersonalDataSMSUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.SaveUserUseCase
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts.Companion.REFRESH_OTP_TIME_MILLIS
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.authentication.utils.Analytics
import es.juntadeandalucia.msspa.authentication.utils.Analytics.PERSONAL_DATA_SMS
import es.juntadeandalucia.msspa.authentication.utils.Analytics.buildEvent
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.exceptions.LoginInvalidRepeatedException
import timber.log.Timber
import javax.crypto.Cipher

class SecondFactorPresenter(
    private var loginPersonalDataUseCase: LoginPersonalDataReinforcedUseCase,
    private val loginPersonalDataSMSUseCase: LoginPersonalDataSMSUseCase,
    private val authorizeUseCase: AuthorizeUseCase,
    private val saveUserUseCase: SaveUserUseCase
) :
    BasePresenter<SecondFactorContract.View>(), SecondFactorContract.Presenter {

    lateinit var config: MsspaAuthenticationConfig
    lateinit var authorize: AuthorizeEntity
    lateinit var msspaAuthenticationUser: MsspaAuthenticationUserEntity
    var saveUser: Boolean = false

    override fun onViewCreated(
        config: MsspaAuthenticationConfig,
        authorize: AuthorizeEntity,
        msspaAuthenticationUser: MsspaAuthenticationUserEntity,
        saveUser: Boolean
    ) {
        this.config = config
        this.authorize = authorize
        this.msspaAuthenticationUser = msspaAuthenticationUser
        this.saveUser = saveUser

        view.setupView()
        view.enableAutoComplete(true)
        view.startTotpCountDown(REFRESH_OTP_TIME_MILLIS)
    }

    override fun onValidateClicked(code: String) {
        tryToLogin(code)
    }

    private fun tryToLogin(pinSms: String) {
        view.showLoading()
        loginPersonalDataSMSUseCase.params(pinSms, authorize)
            .execute(
                onSuccess = {
                    view.apply {
                        sendEvent(buildEvent(PERSONAL_DATA_SMS,true))
                        hideLoading()
                        stopTotpCountDown()
                        if (it.accessToken.isNullOrEmpty()) {
                            setResultError(MsspaAuthenticationError.GENERIC_ERROR)
                        } else {
                            it.authorizeEntity = authorize
                            msspaAuthenticationUser.name = it.msspaAuthenticationUser!!.name
                            if (saveUser) {
                                initSaveUser(msspaAuthenticationUser, it)
                            } else {
                                setResultSuccess(it)
                            }
                        }
                    }
                },
                onError = { error ->
                    view.sendEvent(buildEvent(PERSONAL_DATA_SMS,false))
                    if (error is LoginInvalidRepeatedException) {
                        authorizeUseCase.execute(
                            onSuccess = { newAuthorizeEntity ->
                                this.authorize = newAuthorizeEntity
                                tryToLogin(pinSms)
                            },
                            onError = {
                                view.showRefreshButton()
                                handleCommonErrors(it)
                            }
                        )
                    } else {
                        view.showRefreshButton()
                        handleCommonErrors(error)
                    }
                }
            )
    }

    private fun initSaveUser(
        msspaAuthenticationUser: MsspaAuthenticationUserEntity,
        msspaAuthenticationEntity: MsspaAuthenticationEntity
    ) {
        view.authenticateForEncryption(
            onSuccess = { cipherEncrypt: Cipher?, cipherDecrypt: Cipher? ->
                saveUserUseCase
                    .params(msspaAuthenticationUser, cipherEncrypt!!, cipherDecrypt!!)
                    .execute(
                        onComplete = {
                            Timber.d("User saved successfully")
                            view.setResultSuccess(msspaAuthenticationEntity)
                        },
                        onError = {
                            Timber.e(it)
                            view.setResultSuccess(msspaAuthenticationEntity)
                        }
                    )
            }, onError = {
                Timber.e(it)
                view.setResultSuccess(msspaAuthenticationEntity)
            },
            title = R.string.msspa_auth_user_prompt_save_title,
            subtitle = R.string.msspa_auth_user_prompt_save_subtitle,
            encrypt = true,
            keyString = ApiConstants.KeyNameCipher.KEY_SAVED_USERS,
            isNeedCipher = true
        )
    }

    override fun unsubscribe() {
        loginPersonalDataUseCase.clear()
        loginPersonalDataSMSUseCase.clear()
        saveUserUseCase.clear()
        authorizeUseCase.clear()
    }

    override fun onCodeTextChanged(code: String) {
        if (code.length == ApiConstants.General.CODE_SMS_LENGTH) {
            view.enableContinueButton(true)
        } else {
            view.enableContinueButton(false)
        }
    }

    private fun loadTOTP() {
        with(view) {
            showLoading()
            hideRefreshButton()
            enableContinueButton(false)
        }
        loginPersonalDataUseCase.params(
            msspaAuthenticationUser,
            authorize = authorize,
            loginMethod = ApiConstants.LoginApi.LOGIN_METHOD_DATOS_SMS
        )
        loginPersonalDataUseCase.execute(
            onSuccess = {
                with(view) {
                    hideLoading()
                    showTotpCountdown()
                    startTotpCountDown(REFRESH_OTP_TIME_MILLIS)
                    enableContinueButton(true)
                }
            },
            onError = { error ->
                view.showRefreshButton()
                if (error is LoginInvalidRepeatedException) {
                    authorizeUseCase.execute(
                        onSuccess = { newAuthorizeEntity ->
                            this.authorize = newAuthorizeEntity
                            loadTOTP()
                        },
                        onError = {
                            view.showRefreshButton()
                            handleCommonErrors(it)
                        }
                    )
                } else {
                    view.showRefreshButton()
                    handleCommonErrors(error)
                }
            }
        )
    }

    override fun onCountdownFinish() {
        view.hideTotpCountdown()
        view.showRefreshButton()
    }

    override fun onRefreshClick() {
        loadTOTP()
    }

    override fun onOtherAuthMethodClicked() {
        view.showWarning(MsspaAuthenticationWarning.OTHER_AUTH_METHODS, onCancel = {})
    }
}