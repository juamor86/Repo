package es.juntadeandalucia.msspa.saludandalucia.presentation.help

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter

class HelpPresenter :
    BasePresenter<HelpContract.View>(), HelpContract.Presenter {

    override fun unsubscribe() {
        // Does nothing
    }
}
