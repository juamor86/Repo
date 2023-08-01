package es.inteco.conanmobile.domain.base

import es.inteco.conanmobile.domain.base.scheduler.BaseSchedulerProvider
import es.inteco.conanmobile.domain.base.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Completable use case
 *
 * @property schedulerProvider
 * @constructor Create empty Completable use case
 */
abstract class CompletableUseCase<I>(var schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    fun <T> execute(
        params: I? = null,
        observer: T
    ) where T : Disposable, T : CompletableObserver {
        addDispose(createCompletable(params).subscribeWith(observer))
    }

    fun execute(
        params: I? = null,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        addDispose(createCompletable(params).subscribe(onComplete, onError))
    }

    private fun createCompletable(params: I?) =
        buildUseCase(params).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
    /**
     * Execute
     *
     * @param onComplete
     * @param onError
     * @receiver
     * @receiver
     */
    abstract fun buildUseCase(params: I?): Completable
}
