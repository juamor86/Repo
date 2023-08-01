package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import io.reactivex.Completable

class CreateDelayedNotificationUseCase(val notificationService: NotificationService) :
    CompletableUseCase() {

    private var title: Int = 0
    private var message: Int = 0
    private var graph: Int = 0
    private var dest: Int = 0
    private var delayTime: Long = 0

    override fun buildUseCase(): Completable {
        return Completable.fromAction {
            notificationService.createNotificationDelayed(title, message, graph, dest, delayTime)
        }
    }

    fun params(
        title: Int,
        message: Int,
        graph: Int,
        dest: Int,
        delayTime: Long
    ): CreateDelayedNotificationUseCase = this.apply {
        this.title = title
        this.delayTime = delayTime
        this.graph = graph
        this.dest = dest
        this.message = message
    }
}
