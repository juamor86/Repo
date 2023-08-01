package es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

open class BaseWebviewContract {

    interface View : BaseContract.View {
        fun loadUrl(url: String)
        fun animateWebView()
        fun loadToken(tokenCurrent: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun onWebViewError()
    }
}
