package es.inteco.conanmobile.domain.base

import es.inteco.conanmobile.domain.base.scheduler.BaseSchedulerProvider
import es.inteco.conanmobile.domain.base.scheduler.SchedulerProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject


/**
 * Behavior subject use case
 *
 * @param T
 * @property schedulerProvider
 * @constructor Create empty Behavior subject use case
 */
open class BehaviorSubjectUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    private val publishSubject: BehaviorSubject<T> = BehaviorSubject.create()

    /**
     * Execute
     *
     * @param onNext
     * @param onError
     * @receiver
     * @receiver
     */
    fun execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) =
        addDispose(createPublishSubject().subscribe(onNext, onError))

    /**
     * Publish
     *
     * @param item
     */
    fun publish(item: T) = publishSubject.onNext(item)

    private fun createPublishSubject() =
        publishSubject.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())


}
