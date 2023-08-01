package es.inteco.conanmobile.domain.base.utils

import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import io.reactivex.rxjava3.core.Single

class FakeSuccessSingleUseCase<I, T>(private val mock: T) : SingleUseCase<I, T>(
    TrampolineSchedulerProvider()
) {

    override fun buildUseCase(params: I?): Single<T> = Single.just(mock)
}
