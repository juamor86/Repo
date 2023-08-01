package es.inteco.conanmobile.domain.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Observable use case
 *
 * @param T
 * @constructor Create empty Observable use case
 */
abstract class ObservableUseCase<T> : UseCaseDisposable() {

    /**
     * Execute
     *
     * @param onNext
     * @param onError
     * @param onCompleted
     * @receiver
     * @receiver
     * @receiver
     */
    fun execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onCompleted: () -> Unit
    ) = addDispose(createObservable().subscribe(onNext, onError, onCompleted))

    private fun createObservable() =
        buildUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    /**
     * Build use case
     *
     * @return
     */
    abstract fun buildUseCase(): Observable<T>
}
