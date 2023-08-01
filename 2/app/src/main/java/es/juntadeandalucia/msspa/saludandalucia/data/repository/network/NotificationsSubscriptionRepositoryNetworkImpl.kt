package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPARequestData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NotificationsSubscriptionRepository
import io.reactivex.Completable
import io.reactivex.Single

class NotificationsSubscriptionRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    NotificationsSubscriptionRepository {

    override fun requestVerificationCode(phoneNumber: String): Single<RequestVerificationCodeEntity> =
        msspaApi.requestVerificationCode(
            MSSPARequestData.VerificationCodeRequestData(phoneNumber)
        ).map { verificationCodeResponseData ->
            RequestVerificationCodeEntity(
                verificationCodeResponseData.idVerification, phoneNumber
            )
        }

    override fun subscribe(
        verificationCode: String,
        idVerification: String,
        phone: String,
        firebaseToken: String
    ): Completable =
        msspaApi.subscribeNotifications(
            verificationCode,
            idVerification,
            MSSPARequestData.SubscribeNotificationsRequestData(phone, firebaseToken)
        )

    override fun clearSubscription(phone: String, firebaseToken: String): Completable =
        msspaApi.clearNotificationsSubscription(
            phoneNumber = phone, firebaseToken = firebaseToken
        )

    override fun updateSubscription(
        oldFirebaseToken: String,
        phone: String,
        firebaseToken: String
    ) =
        msspaApi.updateNotificationsSubscription(
            oldFirebaseToken,
            MSSPARequestData.SubscribeNotificationsRequestData(phone, firebaseToken)
        )
}
