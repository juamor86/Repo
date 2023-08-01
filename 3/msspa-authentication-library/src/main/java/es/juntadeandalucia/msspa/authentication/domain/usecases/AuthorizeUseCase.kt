package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.AuthorizeRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.AuthorizeMapper
import io.reactivex.Single

class AuthorizeUseCase(
    private val msspaAuthenticationConfig: MsspaAuthenticationConfig,
    private val authorizeRepositoryFactory: AuthorizeRepositoryFactory
) :
    SingleUseCase<AuthorizeEntity>() {

    override fun buildUseCase(): Single<AuthorizeEntity> =
        authorizeRepositoryFactory.create(Strategy.NETWORK).run {
            authorize(msspaAuthenticationConfig).map {
                AuthorizeMapper.convert(it)
            }
        }
}
