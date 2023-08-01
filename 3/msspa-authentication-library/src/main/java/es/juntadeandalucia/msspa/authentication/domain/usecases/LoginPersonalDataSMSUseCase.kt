package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.LoginMapper
import io.reactivex.Single

class LoginPersonalDataSMSUseCase(
    private val loginRepositoryFactory: LoginPersonalDataRepositoryFactory
) :
SingleUseCase<MsspaAuthenticationEntity>() {

    private lateinit var authorize: AuthorizeEntity
    private lateinit var pinSms: String

    override fun buildUseCase(): Single<MsspaAuthenticationEntity> =
            loginRepositoryFactory.create(Strategy.NETWORK).run {
                loginStep2(authorize.sessionId, authorize.sessionData, pinSms)
            }.map {
                LoginMapper.convert(it, null)
            }

    fun params(pinSms: String, authorize: AuthorizeEntity) =
            this.apply {
                this@LoginPersonalDataSMSUseCase.pinSms = pinSms
                this@LoginPersonalDataSMSUseCase.authorize = authorize
            }
}
