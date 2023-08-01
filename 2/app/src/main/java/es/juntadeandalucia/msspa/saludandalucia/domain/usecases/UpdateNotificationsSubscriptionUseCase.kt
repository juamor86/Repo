package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsSubscriptionRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class UpdateNotificationsSubscriptionUseCase(
    private val notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory,
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory
) :
    CompletableUseCase() {

    private lateinit var newFirebaseToken: String

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getHmsGmsToken()
            .filter { oldToken -> oldToken != newFirebaseToken }
            .flatMapCompletable { oldToken ->
                preferencesRepositoryFactory.create(Strategy.PREFERENCES)
                    .getNotificationsPhoneNumber().flatMapCompletable { phone ->
                        notificationsSubscriptionRepositoryFactory.create(Strategy.NETWORK)
                            .updateSubscription(
                                oldFirebaseToken = oldToken,
                                phone = phone,
                                firebaseToken = newFirebaseToken
                            ).andThen(
                                preferencesRepositoryFactory.create(Strategy.PREFERENCES)
                                    .saveHmsGmsToken(newFirebaseToken)
                            )
                    }
            }

    fun params(firebaseToken: String) =
        this.apply {
            this@UpdateNotificationsSubscriptionUseCase.newFirebaseToken = firebaseToken
        }
}
