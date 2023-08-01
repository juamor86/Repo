package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.NotificationMapper

class RemoveNotificationUseCase(private val notificationsRepositoryFactory: NotificationsRepositoryFactory) :
    CompletableUseCase() {

    lateinit var notification: NotificationEntity

    override fun buildUseCase() = notificationsRepositoryFactory.create(Strategy.DATABASE).run {
        removeNotification(NotificationMapper.convert(notification))
    }

    fun params(notification: NotificationEntity): RemoveNotificationUseCase = this.apply {
        this.notification = notification
    }
}
