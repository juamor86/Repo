package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import io.reactivex.Completable

class RemoveAllNotificationsFromStatusUseCase(val notificationService: NotificationService) :
    CompletableUseCase() {

    override fun buildUseCase(): Completable {
        return Completable.fromAction {
            notificationService.cancelAllNotificationsFromStatusBar()
        }
    }
}
