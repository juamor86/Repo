package es.juntadeandalucia.msspa.saludandalucia.presentation.about

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class AboutPresenter : BasePresenter<AboutContract.View>(), AboutContract.Presenter {
    override fun getScreenNameTracking(): String? = Consts.Analytics.ABOUT_SCREEN_ACCESS

    override fun unsubscribe() {
        // Does nothing
    }


}
