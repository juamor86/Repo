package es.juntadeandalucia.msspa.saludandalucia.domain.base

import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.BaseSchedulerProvider
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.SchedulerProvider
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

open class PublishSubjectUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    private val subject: Subject<T> = PublishSubject.create()

    fun execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) =
        addDispose(createPublishSubject().subscribe(onNext, onError))

    fun publish(item: T) = subject.onNext(item)

    private fun createPublishSubject() =
        subject.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

}

