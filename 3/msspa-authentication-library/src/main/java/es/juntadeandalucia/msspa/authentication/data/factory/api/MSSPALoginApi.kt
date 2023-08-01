package es.juntadeandalucia.msspa.authentication.data.factory.api

import es.juntadeandalucia.msspa.authentication.data.factory.entities.*
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface MSSPALoginApi {
    companion object {
        private const val URL_AUTHORIZE_LOGIN = "${ApiConstants.General.URL_API_V1}/oauth2/authorize/login"
        private const val URL_TOKEN = "${ApiConstants.General.URL_API_V1}/oauth2/token"
        private const val URL_DNI = "https://ws237.sspa.juntadeandalucia.es:443/api/v1.0/oauth2/authorize/login"
        private const val URL_VALIDATE_TOKEN = "${ApiConstants.General.URL_API_V1}/oauth2/token/introspect"
        private const val URL_INVALIDATE_TOKEN = "${ApiConstants.General.URL_API_V1}/oauth/sesiones/{jti}"
    }

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginNoNuhsa(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String,
        @Query(ApiConstants.LoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.LoginApi.IDENTIFIER) id: String,
        @Query(ApiConstants.LoginApi.BIRTHDAY) birthdate: String,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String
    ): Single<LoginResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginBasicNuhsa(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String,
        @Query(ApiConstants.LoginApi.STEP) step: String = ApiConstants.LoginApi.STEP_1,
        @Query(ApiConstants.LoginApi.NUHSA) nuhsa: String,
        @Query(ApiConstants.LoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.LoginApi.IDENTIFIER) id: String,
        @Query(ApiConstants.LoginApi.BIRTHDAY) birthdate: String,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String
    ): Single<LoginResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginBasicNuss(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String,
        @Query(ApiConstants.LoginApi.NUSS) nuss: String,
        @Query(ApiConstants.LoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.LoginApi.IDENTIFIER) id: String,
        @Query(ApiConstants.LoginApi.BIRTHDAY) birthdate: String,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String
    ): Single<LoginResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginReinforced(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String,
        @Query(ApiConstants.LoginApi.STEP) step: String = ApiConstants.LoginApi.STEP_1,
        @Query(ApiConstants.LoginApi.NUHSA) nuhsa: String,
        @Query(ApiConstants.LoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.LoginApi.IDENTIFIER) id: String,
        @Query(ApiConstants.LoginApi.BIRTHDAY) birthdate: String,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.LoginApi.PHONE_NUMBER) phoneNumber: String
    ): Single<LoginReinforcedResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginReinforcedNuss(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String,
        @Query(ApiConstants.LoginApi.STEP) step: String = ApiConstants.LoginApi.STEP_1,
        @Query(ApiConstants.LoginApi.NUSS) nuss: String,
        @Query(ApiConstants.LoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.LoginApi.IDENTIFIER) id: String,
        @Query(ApiConstants.LoginApi.BIRTHDAY) birthdate: String,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.LoginApi.PHONE_NUMBER) phoneNumber: String
    ): Single<LoginReinforcedResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginStep2(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) apiKey: String = ApiConstants.LoginApi.LOGIN_METHOD_DATOS_SMS,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.LoginApi.STEP) step: String = ApiConstants.LoginApi.STEP_2,
        @Query(ApiConstants.LoginApi.PIN_SMS) pin: String
    ): Single<LoginResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginReinforcedValidatePhone(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String = ApiConstants.LoginApi.LOGIN_METHOD_DATOS_SMS,
        @Query(ApiConstants.LoginApi.STEP) step: String = ApiConstants.LoginApi.STEP_1,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.LoginApi.JWT) jwt: String,
        @Query(ApiConstants.LoginApi.PHONE_NUMBER) phoneNumber: String
    ): Single<LoginReinforcedResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginQR(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod: String = ApiConstants.LoginApi.LOGIN_METHOD_QR,
        @Query(ApiConstants.LoginApi.STEP) step: String = ApiConstants.LoginApi.STEP_1,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.LoginApi.QR_ACCESS_TOKEN) qr: String,
        @Query(ApiConstants.LoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.LoginApi.IDENTIFIER) id: String
    ): Single<LoginTOTPResponseData>

    @GET(URL_TOKEN)
    fun refreshToken(
        @Query(ApiConstants.TokenApi.GRANT_TYPE) type: String = ApiConstants.TokenApi.REFRESH_TOKEN_TOTP,
        @Query(ApiConstants.TokenApi.REFRESH_TOKEN) refreshToken: String,
        @Query(ApiConstants.LoginApi.CLIENT_ID) clientId: String,
        @Query(ApiConstants.TokenApi.TOTP) totp: String
    ): Single<LoginRefreshTokenResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginDni(
        @Query(ApiConstants.LoginApi.ACTION) response: String = ApiConstants.LoginApi.ACTION_LOGIN,
        @Query(ApiConstants.LoginApi.LOGIN_METHOD) loginMethod:String = ApiConstants.LoginApi.LOGIN_METHOD_DNIE,
        @Query(ApiConstants.LoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.LoginApi.SESSION_DATA) sessionData: String
    ):Single<LoginResponseData>

    @POST(URL_VALIDATE_TOKEN)
    fun isValidToken(
        @Query(ApiConstants.LoginApi.TOKEN) token: String
    ): Single<LoginResponseData>

    @DELETE(URL_INVALIDATE_TOKEN)
    fun invalidateToken(
        @Path(value = ApiConstants.LoginApi.JTI, encoded = true) jti: String,
        @Header(ApiConstants.General.AUTHORIZATION_HEADER) authorizationToken: String
    ): Completable
}