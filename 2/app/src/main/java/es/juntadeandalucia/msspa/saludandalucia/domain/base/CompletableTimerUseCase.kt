package es.juntadeandalucia.msspa.saludandalucia.domain.base

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.completable.CompletableTimer

abstract class CompletableTimerUseCase : UseCaseDisposable() {

    fun execute(
        onCompleted: () -> Unit,
        onError: (Throwable) -> Unit
    ) = addDispose(createCompletableTimer().subscribe(onCompleted, onError))

    private fun createCompletableTimer() =
        buildUseCase().observeOn(AndroidSchedulers.mainThread())

    abstract fun buildUseCase(): CompletableTimer
}
