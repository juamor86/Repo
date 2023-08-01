package es.juntadeandalucia.msspa.authentication.data.factory.base

import es.juntadeandalucia.msspa.authentication.domain.Strategy

abstract class BaseRepositoryFactory<T> {
    abstract fun create(strategy: Strategy = Strategy.MOCK): T?
}
