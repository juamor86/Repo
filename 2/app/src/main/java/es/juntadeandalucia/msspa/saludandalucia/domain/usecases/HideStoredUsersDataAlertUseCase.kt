package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableTimerUseCase
import io.reactivex.internal.operators.completable.CompletableTimer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HideStoredUsersDataAlertUseCase : CompletableTimerUseCase() {

    companion object {
        private const val DELAY_IN_SECONDS = 5L
    }

    override fun buildUseCase(): CompletableTimer =
        CompletableTimer(DELAY_IN_SECONDS, TimeUnit.SECONDS, Schedulers.io())
}
