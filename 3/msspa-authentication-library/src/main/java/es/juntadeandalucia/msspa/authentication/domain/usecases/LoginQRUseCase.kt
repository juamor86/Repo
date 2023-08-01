package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.LoginMapper
import javax.inject.Inject

class LoginQRUseCase @Inject constructor(
    private val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
) :
    SingleUseCase<MsspaAuthenticationEntity>() {

    private lateinit var authorize: AuthorizeEntity
    private lateinit var qr: String
    private lateinit var idType: String
    private lateinit var id: String


    fun params(
        authorize: AuthorizeEntity,
        qr: String,
        idType: String,
        id: String
    ) {
        this.authorize = authorize
        this.qr = qr
        this.idType = idType
        this.id = id
    }

    override fun buildUseCase() = loginPersonalDataRepositoryFactory.create(Strategy.NETWORK).run {
        loginQR(
            sessionId = authorize.sessionId,
            sessionData = authorize.sessionData,
            qr = qr,
            idType = idType,
            id = id
        ).map {
            LoginMapper.convert(it)
        }
    }
}
