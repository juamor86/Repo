package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsSubscriptionRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.RequestVerificationCodeEntity
import io.reactivex.Single

class RequestVerificationCodeUseCase(private val notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory) :
    SingleUseCase<RequestVerificationCodeEntity>() {

    private lateinit var phone: String

    override fun buildUseCase(): Single<RequestVerificationCodeEntity> =
        notificationsSubscriptionRepositoryFactory.create(Strategy.NETWORK).requestVerificationCode(phone)

    fun params(phone: String) =
        this.apply {
            this@RequestVerificationCodeUseCase.phone = phone
        }
}
