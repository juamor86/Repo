package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsSubscriptionRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationsSubscriptionEntity
import io.reactivex.Completable
import io.reactivex.rxkotlin.zipWith

class ClearNotificationsSubscriptionUseCase(
    private val notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory,
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory
) :
    CompletableUseCase() {

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getHmsGmsToken()
            .zipWith(
                preferencesRepositoryFactory.create(Strategy.PREFERENCES).getNotificationsPhoneNumber()
            ) { firebaseToken, phoneNumber ->
                NotificationsSubscriptionEntity(firebaseToken, phoneNumber)
            }
            .flatMapCompletable { clearNotificationsEntity ->
                notificationsSubscriptionRepositoryFactory.create(Strategy.NETWORK)
                    .clearSubscription(clearNotificationsEntity.phoneNumber, clearNotificationsEntity.firebaseToken)
                    .concatWith(
                        preferencesRepositoryFactory.create(Strategy.PREFERENCES).removeNotificationSubscription()
                    )
            }
}
