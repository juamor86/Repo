package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AnnouncementsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AnnouncementsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AnnouncementsRepository

class AnnouncementsRepositoryFactory(
    private val announcementsRepositoryMockImpl: AnnouncementsRepositoryMockImpl,
    private val announcementsRepositoryNetworkImpl: AnnouncementsRepositoryNetworkImpl
) : BaseRepositoryFactory<AnnouncementsRepository>() {

    override fun create(strategy: Strategy): AnnouncementsRepository =
        when (strategy) {
            Strategy.NETWORK -> announcementsRepositoryNetworkImpl
            else -> announcementsRepositoryMockImpl
        }
}
