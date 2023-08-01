package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.LoginMapper

class LoginPersonalDataBasicUseCase(
    private val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
) :
    SingleUseCase<MsspaAuthenticationEntity>() {

    private lateinit var msspaAuthenticationUser: MsspaAuthenticationUserEntity
    private lateinit var authorize: AuthorizeEntity
    private lateinit var loginMethod: String

    fun params(
        msspaAuthenticationUser: MsspaAuthenticationUserEntity,
        authorize: AuthorizeEntity,
        loginMethod: String
    ) {
        this.apply {
            this.msspaAuthenticationUser = msspaAuthenticationUser
            this.authorize = authorize
            this.loginMethod = loginMethod
        }
    }

    override fun buildUseCase() = loginPersonalDataRepositoryFactory.create(Strategy.NETWORK).run {
        with(msspaAuthenticationUser) {
            if (nuhsa.isNotEmpty()) {
                loginBasicNuhsa(
                    loginMethod = loginMethod,
                    sessionId = authorize.sessionId,
                    sessionData = authorize.sessionData,
                    nuhsa = nuhsa,
                    birthday = birthDate,
                    idType = idType.key,
                    identifier = identification
                ).map { response ->
                    LoginMapper.convert(response, this)
                }
            } else {
                loginBasicNuss(
                    loginMethod = loginMethod,
                    sessionId = authorize.sessionId,
                    sessionData = authorize.sessionData,
                    nuss = nuss,
                    birthday = birthDate,
                    idType = idType.key,
                    identifier = identification
                ).map { response ->
                    LoginMapper.convert(response, this)
                }
            }
        }
    }
}
