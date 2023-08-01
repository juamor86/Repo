package es.inteco.conanmobile.domain.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Maybe use case
 *
 * @param T
 * @constructor Create empty Maybe use case
 */
abstract class MaybeUseCase<T> : UseCaseDisposable() {

    /**
     * Execute
     *
     * @param onSuccess
     * @param onError
     * @param onCompleted
     * @receiver
     * @receiver
     * @receiver
     */
    fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onCompleted: () -> Unit
    ) = addDispose(createMaybe().subscribe(onSuccess, onError, onCompleted))

    private fun createMaybe() =
        buildUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    /**
     * Build use case
     *
     * @return
     */
    abstract fun buildUseCase(): Maybe<T>
}
