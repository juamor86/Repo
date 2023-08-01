package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class SendNotificationReadUseCase(private val notificationsRepositoryFactory: NotificationsRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var idNotification: String

    override fun buildUseCase(): Completable = notificationsRepositoryFactory
        .create(Strategy.NETWORK)
        .sendNotificationRead(idNotification = idNotification)

    fun params(idNotification: String) = this.apply {
        this@SendNotificationReadUseCase.idNotification = idNotification
    }
}
