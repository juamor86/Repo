package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.NotificationsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.NotificationsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence.NotificationsRepositoryDataBaseImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NotificationsRepository

class NotificationsRepositoryFactory(
    private val notificationsRepositoryDataBaseImpl: NotificationsRepositoryDataBaseImpl,
    private val notificationsRepositoryNetworkImpl: NotificationsRepositoryNetworkImpl,
    private val notificationsRepositoryMockImpl: NotificationsRepositoryMockImpl
) : BaseRepositoryFactory<NotificationsRepository>() {

    override fun create(strategy: Strategy): NotificationsRepository =
        when (strategy) {
            Strategy.NETWORK -> notificationsRepositoryNetworkImpl
            Strategy.DATABASE -> notificationsRepositoryDataBaseImpl
            else -> notificationsRepositoryMockImpl
        }
}
