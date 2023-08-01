package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.web

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract

class AuthWebViewContract {

    interface View : BaseContract.View {
        fun loadLoginWebView(url: String)
        fun loadHelpWeb()
        fun configureView()
    }

    interface Presenter : BaseContract.Presenter {
        fun onLoginSuccess(
            tokenType: String,
            accessToken: String,
            expiresIn: String?,
            scope: String
        )

        fun onLoginConfigError()
        fun onCertError()
        fun onNoCertError()
        fun onHelpClick()
        fun onViewCreated(
            config: MsspaAuthenticationConfig,
            level: MsspaAuthenticationManager.Scope
        )

        fun onWebViewError(error: String?)
        fun onAbortInitSession()
        fun resetLoggingQRAttempts()
    }
}
