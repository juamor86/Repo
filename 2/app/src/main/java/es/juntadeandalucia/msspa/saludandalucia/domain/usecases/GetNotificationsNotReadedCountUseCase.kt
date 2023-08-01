package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.FlowableUseCase

class GetNotificationsNotReadedCountUseCase(private val notificationsRepositoryFactory: NotificationsRepositoryFactory) :
    FlowableUseCase<Int>() {

    override fun buildUseCase() = notificationsRepositoryFactory.create(Strategy.DATABASE).run {
        getNotReadedCount()
    }
}
