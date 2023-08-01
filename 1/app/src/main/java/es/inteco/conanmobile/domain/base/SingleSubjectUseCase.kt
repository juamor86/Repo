package es.inteco.conanmobile.domain.base

import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.BaseSchedulerProvider
import es.inteco.conanmobile.domain.base.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.SingleSubject

/**
 * Single subject use case
 *
 * @param T
 * @property schedulerProvider
 * @constructor Create empty Single subject use case
 */
abstract class SingleSubjectUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    private lateinit var strategy: Strategy
    private var subject: SingleSubject<T>? = null

    /**
     * Execute
     *
     * @param onSuccess
     * @param onError
     * @param strategy
     * @receiver
     * @receiver
     */
    fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        strategy: Strategy
    ) =
        addDispose(createSubject(strategy).subscribe(onSuccess, onError))

    private fun createSubject(strategy: Strategy): SingleSubject<T> {
        if (subject == null || this.strategy != strategy) {
            this.strategy = strategy
            subject = SingleSubject.create<T>()
            buildUseCase(strategy).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(subject!!)
        }
        return subject!!
    }

    /**
     * Build use case
     *
     * @param strategy
     * @return
     */
    abstract fun buildUseCase(strategy: Strategy): Single<T>
}
