package es.juntadeandalucia.msspa.authentication.domain

import es.juntadeandalucia.msspa.authentication.domain.base.PublishSubjectUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed

class NavBackPressedBus(
    var navBackPressedBus: NavBackPressed
) :
    PublishSubjectUseCase<NavBackPressed>() {

    fun createNavBackPressed(navType: NavBackPressed.NavBackType) {
        navBackPressedBus.navBackType = navType
        publish(navBackPressedBus)
    }
}
