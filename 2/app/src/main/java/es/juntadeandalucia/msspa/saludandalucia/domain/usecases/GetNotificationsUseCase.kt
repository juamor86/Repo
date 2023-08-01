package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.FlowableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.NotificationMapper

class GetNotificationsUseCase(private val notificationsRepositoryFactory: NotificationsRepositoryFactory) :
    FlowableUseCase<List<NotificationEntity>>() {

    override fun buildUseCase() = notificationsRepositoryFactory.create(Strategy.DATABASE).run {
        getNotifications().map {
            NotificationMapper.convert(it)
        }
    }
}
