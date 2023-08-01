package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.DynamicQuestionnairesRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.DynamicQuestionnairesRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicQuestionnairesRepository

class DynamicQuestionnairesRepositoryFactory(
    private val dynQuestMockImpl: DynamicQuestionnairesRepositoryMockImpl,
    private val dynQuestNetworkImpl: DynamicQuestionnairesRepositoryNetworkImpl
) :
    BaseRepositoryFactory<DynamicQuestionnairesRepository>() {
    override fun create(strategy: Strategy): DynamicQuestionnairesRepository =
        when (strategy) {
            Strategy.NETWORK -> dynQuestNetworkImpl
            else -> dynQuestMockImpl
        }
}