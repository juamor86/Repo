package es.juntadeandalucia.msspa.saludandalucia.presentation.webview

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebViewPresenter

class WebViewPresenter : BaseWebViewPresenter<WebViewContract.View>(), WebViewContract.Presenter {

    override fun onViewCreated(url: String) {
        view.loadUrl(url)
    }
}
