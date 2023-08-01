package es.juntadeandalucia.msspa.saludandalucia.domain.base.utils

import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.TrampolineSchedulerProvider
import io.reactivex.Single

class FakeErrorSingleUseCase<T> : SingleUseCase<T>(TrampolineSchedulerProvider()) {

    override fun buildUseCase(): Single<T> = Single.error(Throwable("Mock error"))
}
