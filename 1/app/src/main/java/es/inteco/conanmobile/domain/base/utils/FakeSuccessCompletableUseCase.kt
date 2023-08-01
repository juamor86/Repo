package es.inteco.conanmobile.domain.base.utils

import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import io.reactivex.rxjava3.core.Completable

class FakeSuccessCompletableUseCase<I> : CompletableUseCase<I>(TrampolineSchedulerProvider()) {

    override fun buildUseCase(params: I?): Completable = Completable.complete()
}
