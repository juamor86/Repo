package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableTimerUseCase
import io.reactivex.internal.operators.completable.CompletableTimer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ShowStoredUsersDataAlertUseCase : CompletableTimerUseCase() {

    companion object {
        private const val DELAY_IN_MILLIS = 500L
    }

    override fun buildUseCase(): CompletableTimer =
        CompletableTimer(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS, Schedulers.io())
}
