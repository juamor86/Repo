package es.juntadeandalucia.msspa.authentication.data.factory.repository.mock

import android.app.Activity
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginRefreshTokenResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginReinforcedResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginTOTPResponseData
import es.juntadeandalucia.msspa.authentication.domain.repository.LoginRepository
import es.juntadeandalucia.msspa.authentication.utils.exceptions.TooManyRequestException
import io.reactivex.Completable
import io.reactivex.Single
import java.security.KeyStore

class LoginMockImpl : LoginRepository {
    override fun loginNoNuhsa(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData> = Single.error(TooManyRequestException())

    override fun loginBasicNuhsa(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData> = Single.error(TooManyRequestException())

    override fun loginBasicNuss(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuss: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData> = Single.error(TooManyRequestException())

    override fun loginReinforced(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData> = Single.error(TooManyRequestException())

    override fun loginReinforcedNuss(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuss: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData> = Single.error(TooManyRequestException())

    override fun loginStep2(
        sessionId: String,
        sessionData: String,
        pinSMS: String
    ): Single<LoginResponseData> = Single.error(TooManyRequestException())

    override fun loginReinforcedValidatePhone(
        sessionId: String,
        sessionData: String,
        jwt: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData> = Single.error(TooManyRequestException())

    override fun loginQR(
        sessionId: String,
        sessionData: String,
        qr: String,
        idType: String,
        id: String
    ): Single<LoginTOTPResponseData> = Single.error(TooManyRequestException())

    override fun refreshToken(
        refreshToken: String,
        clientId: String,
        totp: String
    ): Single<LoginRefreshTokenResponseData> = Single.error(TooManyRequestException())

    override fun loginDni(dniKeyStore: KeyStore, activity: Activity, sessionId: String, sessionData: String): Single<LoginResponseData> =
        Single.error(TooManyRequestException())

    override fun isValidToken(token: String): Single<LoginResponseData> = Single.error(TooManyRequestException())

    override fun invalidateToken(jti: String, authorizationToken:String): Completable = Completable.complete()
}
