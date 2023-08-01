package es.juntadeandalucia.msspa.saludandalucia.domain.bus

import es.juntadeandalucia.msspa.saludandalucia.domain.base.PublishSubjectUseCase

class SmsBus : PublishSubjectUseCase<String>() {

    fun smsReceived(message: String) {
        publish(message)
    }
}