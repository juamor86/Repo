package es.juntadeandalucia.msspa.saludandalucia.data.factory.base

import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy

abstract class BaseRepositoryFactory<T> {

    abstract fun create(strategy: Strategy = Strategy.MOCK): T?
}
