package es.inteco.conanmobile.domain.base

import es.inteco.conanmobile.domain.base.scheduler.BaseSchedulerProvider
import es.inteco.conanmobile.domain.base.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

abstract class SingleUseCase<I, T>(var schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    fun <O> execute(
        params: I? = null,
        observer: O
    ) where O : Disposable, O : SingleObserver<T>{
        addDispose(createSingle(params).subscribeWith(observer))
    }

    fun execute(params: I? = null,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) =
        addDispose(createSingle(params).subscribe(onSuccess, onError))

    private fun createSingle(params: I?) =
        buildUseCase(params).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

    abstract fun buildUseCase(params: I? = null): Single<T>
}
