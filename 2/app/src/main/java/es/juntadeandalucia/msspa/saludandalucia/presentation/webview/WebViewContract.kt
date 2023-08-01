package es.juntadeandalucia.msspa.saludandalucia.presentation.webview

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebviewContract

class WebViewContract {

    interface View : BaseWebviewContract.View

    interface Presenter : BaseWebviewContract.Presenter {
        fun onViewCreated(url: String)
    }
}
