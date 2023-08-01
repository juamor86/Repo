package es.juntadeandalucia.msspa.authentication.domain.base

import es.juntadeandalucia.msspa.authentication.domain.base.scheduler.BaseSchedulerProvider
import es.juntadeandalucia.msspa.authentication.domain.base.scheduler.SchedulerProvider
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

open class BehaviorSubjectUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    private val subject: Subject<T> = BehaviorSubject.create()

    fun execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) =
        addDispose(createPublishSubject().subscribe(onNext, onError))

    fun publish(item: T) = subject.onNext(item)

    private fun createPublishSubject() =
        subject.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
}
