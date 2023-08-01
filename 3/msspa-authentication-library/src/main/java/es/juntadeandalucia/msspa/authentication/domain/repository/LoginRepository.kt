package es.juntadeandalucia.msspa.authentication.domain.repository

import android.app.Activity
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginRefreshTokenResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginReinforcedResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginTOTPResponseData
import es.juntadeandalucia.msspa.authentication.utils.exceptions.TooManyRequestException
import io.reactivex.Completable
import io.reactivex.Single
import java.security.KeyStore
import java.security.cert.X509Certificate

interface LoginRepository {
    fun loginNoNuhsa(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData>

    fun loginBasicNuhsa(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData>

    fun loginBasicNuss(
      loginMethod: String,
      sessionId: String,
      sessionData: String,
      nuss: String,
      birthday: String,
      idType: String,
      identifier: String
  ): Single<LoginResponseData>

    fun loginReinforced(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData>

    fun loginReinforcedNuss(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuss: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData>

    fun loginStep2(
        sessionId: String,
        sessionData: String,
        pinSMS: String
    ): Single<LoginResponseData>

    fun loginReinforcedValidatePhone(
        sessionId: String,
        sessionData: String,
        jwt: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData>

    fun loginQR(
        sessionId: String,
        sessionData: String,
        qr: String,
        idType: String,
        id: String
    ): Single<LoginTOTPResponseData>

    fun refreshToken(
        refreshToken: String,
        clientId: String,
        totp: String
    ): Single<LoginRefreshTokenResponseData>

    fun loginDni(dniKeyStore: KeyStore, activity: Activity, sessionId: String, sessionData: String):Single<LoginResponseData>

    fun isValidToken(token: String): Single<LoginResponseData>

    fun invalidateToken(jti:String, authorizationToken:String) : Completable
}
