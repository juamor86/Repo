package es.juntadeandalucia.msspa.authentication.domain.usecases

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants.LoginApi.JTI
import io.reactivex.Completable
import javax.inject.Inject

class InvalidateTokenUseCase @Inject constructor(
    private val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
) : CompletableUseCase() {

    private lateinit var accessToken: String
    private lateinit var authorizationToken: String

    override fun buildUseCase(): Completable =
        loginPersonalDataRepositoryFactory.create(Strategy.NETWORK).invalidateToken(extractJti(), authorizationToken)

    private fun extractJti(): String {
        val json = String(Base64.decode(accessToken.split(".")[1], Base64.DEFAULT))
        val jsonObj = Gson().fromJson(json, JsonObject::class.java)
        return jsonObj.get(JTI)?.asString ?: ""
    }

    fun params(accessToken: String, authorizationToken: String) =
        this.apply {
            this@InvalidateTokenUseCase.accessToken = accessToken
            this@InvalidateTokenUseCase.authorizationToken = authorizationToken
        }
}