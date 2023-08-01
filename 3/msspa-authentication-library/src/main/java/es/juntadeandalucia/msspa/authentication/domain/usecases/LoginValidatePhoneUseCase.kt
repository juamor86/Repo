package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.LoginReinforcedMapper

class LoginValidatePhoneUseCase(
    private val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
) : SingleUseCase<AuthorizeEntity>() {

    private lateinit var msspaAuthenticationUser: MsspaAuthenticationUserEntity
    private lateinit var authorize: AuthorizeEntity
    private lateinit var jwt: String

    fun params(
        msspaAuthenticationUser: MsspaAuthenticationUserEntity,
        authorize: AuthorizeEntity,
        jwt: String
    ) =
        this.apply {
            this.msspaAuthenticationUser = msspaAuthenticationUser
            this.authorize = authorize
            this.jwt = jwt
        }


    override fun buildUseCase() = loginPersonalDataRepositoryFactory.create(Strategy.NETWORK).run {
        loginReinforcedValidatePhone(
            sessionId = authorize.sessionId,
            sessionData = authorize.sessionData,
            jwt = jwt,
            phoneNumber = msspaAuthenticationUser.phone
        ).map {
            LoginReinforcedMapper.convert(it)
        }
    }
}
