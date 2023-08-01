package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.domain.base.CompletableTimerUseCase
import io.reactivex.internal.operators.completable.CompletableTimer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HideSaveUserDataAlertUseCase : CompletableTimerUseCase() {

    companion object {
        private const val DELAY_IN_SECONDS = 5L
    }

    override fun buildUseCase(): CompletableTimer =
        CompletableTimer(DELAY_IN_SECONDS, TimeUnit.SECONDS, Schedulers.io())
}
