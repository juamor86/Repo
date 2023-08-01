package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AuthorizeRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AuthorizeMapper
import io.reactivex.Single

class AuthorizeUseCase(
    private val authorizeRepositoryFactory: AuthorizeRepositoryFactory
) :
    SingleUseCase<AuthorizeEntity>() {

    override fun buildUseCase(): Single<AuthorizeEntity> =
        authorizeRepositoryFactory.create(Strategy.NETWORK).run {
            authorize().map {
                AuthorizeMapper.convert(it)
            }
        }
}
