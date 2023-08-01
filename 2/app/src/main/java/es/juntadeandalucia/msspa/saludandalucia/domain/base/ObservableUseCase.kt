package es.juntadeandalucia.msspa.saludandalucia.domain.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T> : UseCaseDisposable() {

    fun execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onCompleted: () -> Unit
    ) = addDispose(createObservable().subscribe(onNext, onError, onCompleted))

    private fun createObservable() =
        buildUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    abstract fun buildUseCase(): Observable<T>
}
