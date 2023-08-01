package es.inteco.conanmobile.domain.base.utils

import es.inteco.conanmobile.domain.base.SynchronousUseCase

class FakeSynchronousUseCase<I, T>(private val mock: T) : SynchronousUseCase<I, T>() {

    override fun execute(params: I?): T {
        return mock
    }
}
