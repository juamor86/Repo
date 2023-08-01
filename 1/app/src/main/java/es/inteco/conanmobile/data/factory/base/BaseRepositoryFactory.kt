package es.inteco.conanmobile.data.factory.base

import es.inteco.conanmobile.domain.Strategy


abstract class BaseRepositoryFactory<T> {

    abstract fun create(strategy: Strategy = Strategy.MOCK): T?
}
