package es.juntadeandalucia.msspa.saludandalucia.domain.base.utils

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.TrampolineSchedulerProvider
import io.reactivex.Completable
import java.lang.RuntimeException

class FakeErrorCompletableUseCase : CompletableUseCase(TrampolineSchedulerProvider()) {

    override fun buildUseCase(): Completable = Completable.error(RuntimeException())
}
