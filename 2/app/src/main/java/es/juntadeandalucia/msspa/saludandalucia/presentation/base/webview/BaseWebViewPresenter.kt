package es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview

import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

abstract class BaseWebViewPresenter<V : BaseWebviewContract.View> : BaseWebviewContract.Presenter {

    protected lateinit var view: V

    override fun setViewContract(baseFragment: BaseContract.View) {
        view = baseFragment as V
    }

    override fun onWebViewError() {
        view.showErrorDialogAndFinish(R.string.error_loading_webview)
    }
}
