package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NotificationsSubscriptionRepository
import io.reactivex.Completable
import io.reactivex.Single

class NotificationsSubscriptionRepositoryMockImpl(val context: Context) :
    NotificationsSubscriptionRepository {

    override fun requestVerificationCode(phoneNumber: String): Single<RequestVerificationCodeEntity> =
        Single.just(
            RequestVerificationCodeEntity("123456789", "123456789")
        )

    override fun subscribe(
        verificationCode: String,
        idVerification: String,
        phone: String,
        firebaseToken: String
    ): Completable =
        Completable.complete()

    override fun clearSubscription(phone: String, firebaseToken: String): Completable =
        Completable.complete()

    override fun updateSubscription(
        oldFirebaseToken: String,
        phone: String,
        firebaseToken: String
    ): Completable =
        Completable.complete()
}
