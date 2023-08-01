package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.web

import androidx.core.text.isDigitsOnly
import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.domain.usecases.SetLoggingQRAttemptsUseCase
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import timber.log.Timber

class AuthWebViewPresenter(val navBackPressedBus: NavBackPressedBus,
                           val setLoggingQRAttemptsUseCase: SetLoggingQRAttemptsUseCase) :
    BasePresenter<AuthWebViewContract.View>(), AuthWebViewContract.Presenter {

    private lateinit var config: MsspaAuthenticationConfig
    private lateinit var level: MsspaAuthenticationManager.Scope

    override fun onViewCreated(
        config: MsspaAuthenticationConfig,
        level: MsspaAuthenticationManager.Scope
    ) {
        this.config = config
        this.level = level
        with(view) {
            configureView()
            loadLoginWebView(MsspaAuthConsts.getUrl(config, level))
        }
        with(navBackPressedBus) {
            execute(onNext = { navBackPressedBus->
                if(navBackPressedBus.navBackType == NavBackPressed.NavBackType.WOA_SCREEN) {
                    onAbortInitSession()
                }
            }, onError = {
                Timber.e(it)
            })
        }
    }

    override fun onLoginSuccess(
        tokenType: String,
        accessToken: String,
        expiresIn: String?,
        scope: String
    ) {
        val result = MsspaAuthenticationEntity(
            tokenType,
            accessToken,
            if (expiresIn != null && expiresIn.isDigitsOnly()) {
                Integer.parseInt(
                    expiresIn
                )
            } else {
                null
            },
            MsspaAuthenticationManager.Scope.getScope(scope),
            null,
            null, null, null
        )
        view.setResultSuccess(result)
    }

    override fun onLoginConfigError() {
        view.setResultError(MsspaAuthenticationError.WRONG_CONFIG)
    }

    override fun onCertError() {
        view.showError(MsspaAuthenticationError.INVALID_CERTIFICATE)
        view.loadLoginWebView(MsspaAuthConsts.getUrl(config, level))
    }

    override fun onNoCertError() {
        view.showError(MsspaAuthenticationError.NO_CERTIFICATE)
        view.loadLoginWebView(MsspaAuthConsts.getUrl(config, level))
    }


    override fun onWebViewError(error: String?) {
        view.setResultError(MsspaAuthenticationError.LOADING_WEB)
    }

    override fun onHelpClick() {
        view.loadHelpWeb()
    }

    override fun onAbortInitSession() {
        view.setResultError(MsspaAuthenticationError.ABORT_INIT_LOGIN)
    }

    override fun resetLoggingQRAttempts() {
        setLoggingQRAttemptsUseCase.params(true)
        setLoggingQRAttemptsUseCase.execute(
            onComplete = {
                Timber.d("Reset logging QRAttempts")
            },
            onError = {
                Timber.e(it)
            }
        )
    }

    override fun unsubscribe() {
        // Does nothing
    }


}