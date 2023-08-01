package es.juntadeandalucia.msspa.saludandalucia.presentation.splash

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    private fun delayAndNavigateHome() {
        Observable
            .timer(Consts.SPLASH_DELAY, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .map<Any> { view.navigateToHomeScreen() }
            .subscribe()
    }

    override fun onCreate() {
        view.startTextAnimation()
        delayAndNavigateHome()
    }

    override fun unsubscribe() {
    }
}
