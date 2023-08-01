package es.juntadeandalucia.msspa.saludandalucia.domain.base

import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.BaseSchedulerProvider
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.SchedulerProvider
import io.reactivex.Completable

abstract class CompletableUseCase(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    fun execute(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ) = addDispose(createCompletable().subscribe(onComplete, onError))

    private fun createCompletable() =
        buildUseCase().subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

    abstract fun buildUseCase(): Completable
}
