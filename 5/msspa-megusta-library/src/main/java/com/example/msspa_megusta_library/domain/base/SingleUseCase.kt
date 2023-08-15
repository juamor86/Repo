package com.example.msspa_megusta_library.domain.base

import com.example.msspa_megusta_library.domain.base.schedulers.BaseSchedulerProvider
import com.example.msspa_megusta_library.domain.base.schedulers.SchedulerProvider
import io.reactivex.Single

abstract class SingleUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    UseCaseDisposable() {

    fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) =
        addDispose(createSingle().subscribe(onSuccess, onError))

    private fun createSingle() =
        buildUseCase().subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

    abstract fun buildUseCase(): Single<T>
}
