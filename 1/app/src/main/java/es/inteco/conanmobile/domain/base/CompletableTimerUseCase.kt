package es.inteco.conanmobile.domain.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.internal.operators.completable.CompletableTimer


/**
 * Completable timer use case
 *
 * @constructor Create empty Completable timer use case
 */
abstract class CompletableTimerUseCase : UseCaseDisposable() {

    /**
     * Execute
     *
     * @param onCompleted
     * @param onError
     * @receiver
     * @receiver
     */
    fun execute(
        onCompleted: () -> Unit,
        onError: (Throwable) -> Unit
    ) = addDispose(createCompletableTimer().subscribe(onCompleted, onError))

    private fun createCompletableTimer() =
        buildUseCase().observeOn(AndroidSchedulers.mainThread())

    /**
     * Build use case
     *
     * @return
     */
    abstract fun buildUseCase(): CompletableTimer
}
