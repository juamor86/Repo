package es.inteco.conanmobile.data.factory

import es.inteco.conanmobile.data.factory.base.BaseRepositoryFactory
import es.inteco.conanmobile.data.repository.mock.IncibeRepositoryMockImpl
import es.inteco.conanmobile.data.repository.network.IncibeRepositoryNetworkImpl
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.repository.IncibeRepository

/**
 * Incibe repository factory
 *
 * @property incibeRepositoryNetworkImpl
 * @property incibeRepositoryMockImpl
 * @constructor Create empty Incibe repository factory
 */
class IncibeRepositoryFactory (
    private val incibeRepositoryNetworkImpl: IncibeRepositoryNetworkImpl,
    private val incibeRepositoryMockImpl: IncibeRepositoryMockImpl
        ) : BaseRepositoryFactory<IncibeRepository>(){

    override fun create(strategy: Strategy): IncibeRepository =
        when (strategy){
            Strategy.NETWORK -> incibeRepositoryNetworkImpl
            else -> incibeRepositoryMockImpl
        }
}