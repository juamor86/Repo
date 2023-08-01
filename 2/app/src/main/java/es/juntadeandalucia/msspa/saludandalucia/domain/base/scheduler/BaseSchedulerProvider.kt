package es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler

import io.reactivex.Scheduler

interface BaseSchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}
