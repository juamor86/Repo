package es.juntadeandalucia.msspa.saludandalucia.domain.base

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class FlowableUseCase<T> : UseCaseDisposable() {

    fun execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onCompleted: () -> Unit
    ) = addDispose(createFlowable().subscribe(onNext, onError, onCompleted))

    private fun createFlowable() =
        buildUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    abstract fun buildUseCase(): Flowable<T>
}
