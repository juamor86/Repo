package es.juntadeandalucia.msspa.saludandalucia.domain.bus

import es.juntadeandalucia.msspa.saludandalucia.domain.base.PublishSubjectUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LoginAdvicePressed

class LoginAdvicePressedBus(
    var loginAdvicePressedBus: LoginAdvicePressed
) :
    PublishSubjectUseCase<LoginAdvicePressed>() {

    fun createLoginAdvicePressed() {
        publish(loginAdvicePressedBus)
    }
}
