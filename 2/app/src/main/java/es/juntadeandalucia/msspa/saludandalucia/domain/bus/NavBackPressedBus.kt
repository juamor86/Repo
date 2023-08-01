package es.juntadeandalucia.msspa.saludandalucia.domain.bus

import es.juntadeandalucia.msspa.saludandalucia.domain.base.PublishSubjectUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NavBackPressed

class NavBackPressedBus(
    var navBackPressedBus: NavBackPressed
) :
    PublishSubjectUseCase<NavBackPressed>() {

    fun createNavBackPressed(isNavBackPressedBus: Boolean) {
        navBackPressedBus.isNavBackPressed = isNavBackPressedBus
        publish(navBackPressedBus)
    }

}

