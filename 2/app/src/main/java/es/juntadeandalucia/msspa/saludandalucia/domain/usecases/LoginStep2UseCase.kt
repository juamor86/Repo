package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.LoginRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LoginEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.LoginMapper
import io.reactivex.Single

class LoginStep2UseCase(
    private val loginRepositoryFactory: LoginRepositoryFactory
) :
    SingleUseCase<LoginEntity>() {

    private lateinit var authorize: AuthorizeEntity
    private lateinit var pinSms: String

    override fun buildUseCase(): Single<LoginEntity> =
        loginRepositoryFactory.create(Strategy.NETWORK).run {
            loginStep2(authorize.sessionId, authorize.sessionData, pinSms)
        }
            .map {
                LoginMapper.convert(it)
            }

    fun params(pinSms: String, authorize: AuthorizeEntity) =
        this.apply {
            this@LoginStep2UseCase.pinSms = pinSms
            this@LoginStep2UseCase.authorize = authorize
        }
}
