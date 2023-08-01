package es.inteco.conanmobile.domain.base.utils

import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import io.reactivex.rxjava3.core.Single

class FakeErrorSingleUseCase<I, T> : SingleUseCase<I, T>(TrampolineSchedulerProvider()) {

    override fun buildUseCase(params: I?): Single<T> = Single.error(Throwable("Mock error"))
}
