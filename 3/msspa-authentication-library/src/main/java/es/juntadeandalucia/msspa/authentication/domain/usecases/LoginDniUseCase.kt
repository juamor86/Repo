package es.juntadeandalucia.msspa.authentication.domain.usecases

import android.app.Activity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.LoginMapper
import io.reactivex.Single
import java.security.KeyStore
import javax.inject.Inject

class LoginDniUseCase @Inject constructor(
    private val loginRepositoryFactory: LoginPersonalDataRepositoryFactory
) :
    SingleUseCase<MsspaAuthenticationEntity>() {

    private lateinit var authorize: AuthorizeEntity
    private lateinit var keyStore: KeyStore
    private lateinit var activity: Activity
    private lateinit var msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity

    override fun buildUseCase(): Single<MsspaAuthenticationEntity> =
        loginRepositoryFactory.create(Strategy.NETWORK).run {
            loginDni(keyStore, activity, authorize.sessionId, authorize.sessionData)
        }.map {
            LoginMapper.convert(it, msspaAuthenticationUserEntity)
        }

    fun params(keyStore: KeyStore, activity: Activity, msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity, authorize:AuthorizeEntity) =
        this.apply {
            this@LoginDniUseCase.keyStore = keyStore
            this@LoginDniUseCase.activity = activity
            this@LoginDniUseCase.authorize = authorize
            this@LoginDniUseCase.msspaAuthenticationUserEntity = msspaAuthenticationUserEntity
        }
}
