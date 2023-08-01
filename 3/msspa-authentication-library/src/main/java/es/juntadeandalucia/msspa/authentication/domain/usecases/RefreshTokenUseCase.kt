package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.mappers.LoginMapper
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
) :
    SingleUseCase<MsspaAuthenticationEntity>() {

    private lateinit var refreshToken: String
    private lateinit var clientId: String
    private lateinit var totp: String


    fun params(
        refreshToken: String,
        clientId: String,
        totp: String
    ): RefreshTokenUseCase {

        this.refreshToken = refreshToken
        this.clientId = clientId
        this.totp = totp

        return this
    }

    override fun buildUseCase() = loginPersonalDataRepositoryFactory.create(Strategy.NETWORK).run {
        refreshToken(
            refreshToken = refreshToken, clientId = clientId, totp = totp
        ).map {
            LoginMapper.convert(it)
        }
    }
}
