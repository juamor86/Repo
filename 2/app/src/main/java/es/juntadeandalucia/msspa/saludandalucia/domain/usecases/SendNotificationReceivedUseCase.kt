package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class SendNotificationReceivedUseCase @Inject constructor(private val notificationsRepositoryFactory: NotificationsRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var idNotification: String

    override fun buildUseCase(): Completable = notificationsRepositoryFactory
        .create(Strategy.NETWORK)
        .sendNotificationReceived(idNotification = idNotification)

    fun params(idNotification: String) = this.apply {
        this@SendNotificationReceivedUseCase.idNotification = idNotification
    }
}
