package es.juntadeandalucia.msspa.authentication.data.factory.repository.network

import android.app.Activity
import es.gob.fnmt.dniedroid.net.ssl.DNIeSSLSocketFactory
import es.gob.fnmt.dniedroid.policy.KeyManagerPolicy
import es.gob.jmulticard.jse.provider.DnieProvider
import es.juntadeandalucia.msspa.authentication.data.factory.api.MSSPALoginApi
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginRefreshTokenResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginReinforcedResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginTOTPResponseData
import es.juntadeandalucia.msspa.authentication.domain.repository.LoginRepository
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import java.net.URL

class LoginImpl(
    private val msspaLoginApi: MSSPALoginApi,
    private val okHttpClient: OkHttpClient,
    private val retrofit: Retrofit
) : LoginRepository {

    override fun loginNoNuhsa(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData> =
        msspaLoginApi.loginNoNuhsa(
            loginMethod = loginMethod,
            sessionId = sessionId,
            sessionData = sessionData,
            idType = idType,
            id = identifier,
            birthdate = birthday
        )

    override fun loginBasicNuhsa(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData> =
        msspaLoginApi.loginBasicNuhsa(
            loginMethod = loginMethod,
            sessionId = sessionId,
            sessionData = sessionData,
            idType = idType,
            id = identifier,
            nuhsa = nuhsa,
            birthdate = birthday
        )

    override fun loginBasicNuss(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuss: String,
        birthday: String,
        idType: String,
        identifier: String
    ): Single<LoginResponseData> =
        msspaLoginApi.loginBasicNuss(
            loginMethod = loginMethod,
            sessionId = sessionId,
            sessionData = sessionData,
            idType = idType,
            id = identifier,
            nuss = nuss,
            birthdate = birthday
        )

    override fun loginReinforced(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData> =
        msspaLoginApi.loginReinforced(
            loginMethod = loginMethod,
            sessionId = sessionId,
            sessionData = sessionData,
            idType = idType,
            id = identifier,
            nuhsa = nuhsa,
            birthdate = birthday,
            phoneNumber = "+34$phoneNumber"
        )

    override fun loginReinforcedNuss(
        loginMethod: String,
        sessionId: String,
        sessionData: String,
        nuss: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData> =
        msspaLoginApi.loginReinforcedNuss(
            loginMethod = loginMethod,
            sessionId = sessionId,
            sessionData = sessionData,
            idType = idType,
            id = identifier,
            nuss = nuss,
            birthdate = birthday,
            phoneNumber = "+34$phoneNumber"
        )

    override fun loginStep2(
        sessionId: String,
        sessionData: String,
        pinSMS: String
    ): Single<LoginResponseData> =
        msspaLoginApi.loginStep2(
            sessionId = sessionId,
            sessionData = sessionData,
            pin = pinSMS
        )

    override fun loginReinforcedValidatePhone(
        sessionId: String,
        sessionData: String,
        jwt: String,
        phoneNumber: String
    ): Single<LoginReinforcedResponseData> =
        msspaLoginApi.loginReinforcedValidatePhone(
            sessionId = sessionId,
            sessionData = sessionData,
            jwt = jwt,
            phoneNumber = "+34$phoneNumber"
        )

    override fun loginQR(
        sessionId: String,
        sessionData: String,
        qr: String,
        idType: String,
        id: String
    ): Single<LoginTOTPResponseData> =
        msspaLoginApi.loginQR(
            sessionId = sessionId,
            sessionData = sessionData,
            qr = qr,
            id = id,
            idType = idType
        )

    override fun refreshToken(
        refreshToken: String,
        clientId: String,
        totp: String
    ): Single<LoginRefreshTokenResponseData> =
        msspaLoginApi.refreshToken(
            refreshToken = refreshToken, clientId = clientId, totp = totp
        )

    override fun loginDni(dniKeyStore: KeyStore, activity:Activity, sessionId: String, sessionData: String): Single<LoginResponseData> {
        val dnieSSLSocketFactory =
            DNIeSSLSocketFactory.getInstance(
                dniKeyStore,
                null,
                KeyManagerPolicy.getBuilder().addAlias(DnieProvider.AUTH_CERT_ALIAS)
                    .addKeyUsage(KeyManagerPolicy.KeyUsage.digitalSignature)
                    .build(),
                activity
            )
        val dni = (dniKeyStore.getCertificate(DnieProvider.AUTH_CERT_ALIAS) as X509Certificate).toString()
        Timber.d("Certificado del dni: %s", dni)
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).init(null as KeyStore?)
        val client = okHttpClient.newBuilder().sslSocketFactory(dnieSSLSocketFactory, getDefaultTrustManager()).build()
        var baseUrl = retrofit.baseUrl()
        val url = URL(baseUrl.scheme, baseUrl.host, 443, "")
        val api = retrofit.newBuilder().baseUrl(url.toString()).client(client).build().create(MSSPALoginApi::class.java)

        return api.loginDni(sessionId = sessionId, sessionData = sessionData)
    }

    private fun getDefaultTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:"
                    + trustManagers.contentToString())
        }
        return trustManagers[0] as X509TrustManager
    }

    override fun isValidToken(token: String): Single<LoginResponseData> {
        return msspaLoginApi.isValidToken(token = token)
    }

    override fun invalidateToken(jti: String, authorizationToken: String): Completable {
        return msspaLoginApi.invalidateToken(jti = jti, authorizationToken = authorizationToken)
    }
}