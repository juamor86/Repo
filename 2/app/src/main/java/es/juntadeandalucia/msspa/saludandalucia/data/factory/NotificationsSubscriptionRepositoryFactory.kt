package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.NotificationsSubscriptionRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.NotificationsSubscriptionRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NotificationsSubscriptionRepository

class NotificationsSubscriptionRepositoryFactory(
    private val notificationsRepositoryNetworkImpl: NotificationsSubscriptionRepositoryNetworkImpl,
    private val notificationsSubscriptionRepositoryMockImpl: NotificationsSubscriptionRepositoryMockImpl
) : BaseRepositoryFactory<NotificationsSubscriptionRepository>() {

    override fun create(strategy: Strategy): NotificationsSubscriptionRepository =
        when (strategy) {
            Strategy.NETWORK -> notificationsRepositoryNetworkImpl
            else -> notificationsSubscriptionRepositoryMockImpl
        }
}
