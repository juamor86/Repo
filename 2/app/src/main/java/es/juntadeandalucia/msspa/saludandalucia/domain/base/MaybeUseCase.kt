package es.juntadeandalucia.msspa.saludandalucia.domain.base

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class MaybeUseCase<T> : UseCaseDisposable() {

    fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onCompleted: () -> Unit
    ) = addDispose(createMaybe().subscribe(onSuccess, onError, onCompleted))

    private fun createMaybe() =
        buildUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    abstract fun buildUseCase(): Maybe<T>
}
