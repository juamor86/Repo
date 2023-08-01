package es.juntadeandalucia.msspa.saludandalucia.domain.base

import android.webkit.HttpAuthHandler
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.BaseSchedulerProvider
import es.juntadeandalucia.msspa.saludandalucia.domain.base.scheduler.SchedulerProvider
import javax.inject.Inject

abstract class HttpSingleUseCase<T>(private val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()) :
    SingleUseCase<T>() {

    @Inject
    lateinit var httpHandler: HttpAuthHandler

    override fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Boolean {

        return addDispose(createSingle().subscribe(onSuccess, this::onError))
    }

    private fun createSingle() =
        buildUseCase().subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

    fun onError(throwable: Throwable) {
    }
}
