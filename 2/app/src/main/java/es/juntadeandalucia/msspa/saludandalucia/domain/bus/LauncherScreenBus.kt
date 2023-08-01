package es.juntadeandalucia.msspa.saludandalucia.domain.bus

import es.juntadeandalucia.msspa.saludandalucia.domain.base.BehaviorSubjectUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.PublishSubjectUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LauncherScreen

class LauncherScreenBus(
    var launcherScreen: LauncherScreen
) :
    BehaviorSubjectUseCase<LauncherScreen>() {

    fun createLauncherScreen(nextScreen: LauncherScreen.LauncherScreenTypes) {
        launcherScreen.launcherScreenType = nextScreen
        publish(launcherScreen)
    }
}
