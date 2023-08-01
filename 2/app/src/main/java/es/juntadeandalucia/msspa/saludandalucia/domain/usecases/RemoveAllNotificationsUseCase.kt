package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase

class RemoveAllNotificationsUseCase(private val notificationsRepositoryFactory: NotificationsRepositoryFactory) :
    CompletableUseCase() {

    lateinit var notification: NotificationData

    override fun buildUseCase() = notificationsRepositoryFactory.create(Strategy.DATABASE).run {
        removeAllNotifications()
    }
}
