package es.juntadeandalucia.msspa.authentication.domain.base

abstract class SynchronousUseCase<T>() {
    abstract fun execute(): T
}
