package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.webview

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebviewContract

class ClicSaludWebViewContract {

    interface View : BaseWebviewContract.View {
        fun loadUrl(url: String, header: Pair<String, String>? = null)
        fun setupView(title: String)
    }

    interface Presenter : BaseWebviewContract.Presenter {
        fun onCreate()
        fun onViewCreated(navigationEntity: NavigationEntity)
    }
}
