package es.juntadeandalucia.msspa.saludandalucia.domain.base

import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.BaseSchedulerProvider
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.SchedulerProvider
import io.reactivex.Single

abstract class SingleUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    open fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) =
        addDispose(createSingle().subscribe(onSuccess, onError))

    private fun createSingle() =
        buildUseCase().subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

    abstract fun buildUseCase(): Single<T>
}
