package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.LoginRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LoginEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.LoginMapper
import io.reactivex.Single

class LoginStep1UseCase(
    private val loginRepositoryFactory: LoginRepositoryFactory
) :
    SingleUseCase<LoginEntity>() {

    private lateinit var user: QuizUserEntity
    private lateinit var authorize: AuthorizeEntity

    override fun buildUseCase(): Single<LoginEntity> =
        loginRepositoryFactory.create(Strategy.NETWORK).run {
            loginStep1(
                authorize.sessionId,
                authorize.sessionData,
                user.nuhsa,
                user.birthDate,
                user.idType.key,
                user.identification,
                user.prefixPhone.plus(user.phone)
            )
        }
            .map {
                LoginMapper.convert(it)
            }

    fun params(
        user: QuizUserEntity,
        authorize: AuthorizeEntity
    ) =
        this.apply {
            this@LoginStep1UseCase.user = user
            this@LoginStep1UseCase.authorize = authorize
        }
}
