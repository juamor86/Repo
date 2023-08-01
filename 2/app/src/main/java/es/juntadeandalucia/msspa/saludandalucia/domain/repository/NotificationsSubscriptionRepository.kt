package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import io.reactivex.Completable
import io.reactivex.Single

interface NotificationsSubscriptionRepository {
    fun requestVerificationCode(phoneNumber: String): Single<RequestVerificationCodeEntity>
    fun subscribe(
        verificationCode: String,
        idVerification: String,
        phone: String,
        firebaseToken: String
    ): Completable

    fun clearSubscription(phone: String, firebaseToken: String): Completable
    fun updateSubscription(
        oldFirebaseToken: String,
        phone: String,
        firebaseToken: String
    ): Completable
}
