package es.inteco.conanmobile.domain.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Flowable use case
 *
 * @param T
 * @constructor Create empty Flowable use case
 */
abstract class FlowableUseCase<T> : UseCaseDisposable() {

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
    ) = addDispose(createFlowable().subscribe(onNext, onError, onCompleted))

    private fun createFlowable() =
        buildUseCase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    /**
     * Build use case
     *
     * @return
     */
    abstract fun buildUseCase(): Flowable<T>
}
