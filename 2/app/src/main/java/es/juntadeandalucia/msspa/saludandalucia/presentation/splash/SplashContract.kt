package es.juntadeandalucia.msspa.saludandalucia.presentation.splash

import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class SplashContract {

    interface View : BaseContract.View {
        fun navigateToHomeScreen()
        fun startTextAnimation()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate()
    }
}
