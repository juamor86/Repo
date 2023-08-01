package es.inteco.conanmobile.domain.base

abstract class SynchronousUseCase<I, T> {
    abstract fun execute(params: I? = null): T
}
