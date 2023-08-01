package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsSubscriptionRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class SubscribeNotificationsUseCase(
    private val notificationsRepositoryFactory: NotificationsSubscriptionRepositoryFactory,
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory
) :
    CompletableUseCase() {

    private lateinit var verificationCode: String
    private lateinit var idVerification: String
    private lateinit var phone: String

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getHmsGmsToken()
            .flatMapCompletable { firebaseToken ->
                notificationsRepositoryFactory
                    .create(Strategy.NETWORK)
                    .subscribe(verificationCode, idVerification, phone, firebaseToken)
                    .concatWith(
                        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveNotificationSubscription(phone)
                    )
            }

    fun params(verificationCode: String, idVerification: String, phone: String) =
        this.apply {
            this@SubscribeNotificationsUseCase.verificationCode = verificationCode
            this@SubscribeNotificationsUseCase.idVerification = idVerification
            this@SubscribeNotificationsUseCase.phone = phone
        }
}
