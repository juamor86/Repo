package es.inteco.conanmobile.data.factory

import es.inteco.conanmobile.data.factory.base.BaseRepositoryFactory
import es.inteco.conanmobile.data.repository.mock.AnalysisRepositoryMockImpl
import es.inteco.conanmobile.data.repository.preferences.AnalysisRepositoryPreferencesImpl
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.repository.AnalysisRepository

/**
 * Analysis repository factory
 *
 * @property analysisRepositoryMockImpl
 * @property analysisRepositoryPreferencesImpl
 * @constructor Create empty Analysis repository factory
 */
class AnalysisRepositoryFactory(
    private val analysisRepositoryMockImpl: AnalysisRepositoryMockImpl,
    private val analysisRepositoryPreferencesImpl: AnalysisRepositoryPreferencesImpl
) : BaseRepositoryFactory<AnalysisRepository>() {

    override fun create(strategy: Strategy): AnalysisRepository =
        when (strategy) {
            Strategy.PREFERENCES -> analysisRepositoryPreferencesImpl
            else -> analysisRepositoryMockImpl
        }
}