package es.inteco.conanmobile.data.factory

import es.inteco.conanmobile.data.factory.base.BaseRepositoryFactory
import es.inteco.conanmobile.data.repository.mock.DefaultAnalysisRepositoryMockImpl
import es.inteco.conanmobile.data.repository.network.DefaultAnalysisRepositoryNetworkImpl
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.repository.DefaultAnalysisRepository

/**
 * Default analysis repository factory
 *
 * @property defaultAnalysisRepositoryMockImpl
 * @property defaultAnalysisRepositoryNetworkImpl
 * @constructor Create empty Default analysis repository factory
 */
class DefaultAnalysisRepositoryFactory(
    private val defaultAnalysisRepositoryMockImpl: DefaultAnalysisRepositoryMockImpl,
    private val defaultAnalysisRepositoryNetworkImpl: DefaultAnalysisRepositoryNetworkImpl
) : BaseRepositoryFactory<DefaultAnalysisRepository>() {

    override fun create(strategy: Strategy): DefaultAnalysisRepository =
        when (strategy) {
            Strategy.NETWORK -> defaultAnalysisRepositoryNetworkImpl
            else -> defaultAnalysisRepositoryMockImpl
        }
}