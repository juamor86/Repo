package es.juntadeandalucia.msspa.saludandalucia.domain.base

abstract class SynchronousUseCase<T>() {
    abstract fun execute(): T
}
