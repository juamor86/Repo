package es.juntadeandalucia.msspa.saludandalucia.presentation.main.fragment

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter

class MainFragmentPresenter : BasePresenter<MainFragmentContract.View>(), MainFragmentContract.Presenter {

    override fun unsubscribe() {
        // Does nothing
    }
}
