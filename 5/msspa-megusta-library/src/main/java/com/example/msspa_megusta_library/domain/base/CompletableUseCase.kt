package com.example.msspa_megusta_library.domain.base

import com.example.msspa_megusta_library.domain.base.schedulers.BaseSchedulerProvider
import com.example.msspa_megusta_library.domain.base.schedulers.SchedulerProvider
import io.reactivex.Completable

abstract class CompletableUseCase(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
        UseCaseDisposable() {

    fun execute(
            onComplete: () -> Unit,
            onError: (Throwable) -> Unit
    ) = addDispose(createCompletable().subscribe(onComplete, onError))

    private fun createCompletable() =
            buildUseCase().subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

    abstract fun buildUseCase(): Completable
}
