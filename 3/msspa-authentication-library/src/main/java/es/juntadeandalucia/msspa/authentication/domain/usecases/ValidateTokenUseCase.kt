package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(
    private val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
) : SingleUseCase<Boolean>() {

    private lateinit var accessToken: String

    override fun buildUseCase(): Single<Boolean> =
        loginPersonalDataRepositoryFactory.create(Strategy.NETWORK).run {
            isValidToken(accessToken)
        }.map {
            it.active
        }

    fun params(accessToken: String) =
        this.apply {
            this@ValidateTokenUseCase.accessToken = accessToken
        }
}
