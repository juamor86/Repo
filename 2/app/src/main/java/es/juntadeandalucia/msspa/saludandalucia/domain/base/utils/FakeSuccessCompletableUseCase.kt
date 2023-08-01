package es.juntadeandalucia.msspa.saludandalucia.domain.base.utils

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.TrampolineSchedulerProvider
import io.reactivex.Completable

class FakeSuccessCompletableUseCase : CompletableUseCase(TrampolineSchedulerProvider()) {

    override fun buildUseCase(): Completable = Completable.complete()
}
