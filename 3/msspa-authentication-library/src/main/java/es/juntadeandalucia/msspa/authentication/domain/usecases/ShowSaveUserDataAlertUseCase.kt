package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.domain.base.CompletableTimerUseCase
import io.reactivex.internal.operators.completable.CompletableTimer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ShowSaveUserDataAlertUseCase : CompletableTimerUseCase() {

    companion object {
        private const val DELAY_IN_MILLIS = 500L
    }

    override fun buildUseCase(): CompletableTimer =
        CompletableTimer(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS, Schedulers.io())
}
