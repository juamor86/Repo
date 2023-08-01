package es.juntadeandalucia.msspa.saludandalucia.data.api

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.LoginResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MSSPALoginApi {

    companion object {
        private const val URL_AUTHORIZE = "${ApiConstants.General.URL_API_V1}/oauth2/authorize"
        private const val URL_AUTHORIZE_LOGIN =
            "${ApiConstants.General.URL_API_V1}/oauth2/authorize/login"
    }

    @GET(URL_AUTHORIZE)
    fun authorize(
        @Query(ApiConstants.QuizLoginApi.RESPONSE_TYPE) response: String = ApiConstants.QuizLoginApi.RESPONSE_TYPE_CODE,
        @Query(ApiConstants.QuizLoginApi.CLIENT_ID) apiKey: String = ApiConstants.General.SALUD_ANDALUCIA_LEGACY_APP_IDENTIFICATION,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE,
        @Query(ApiConstants.QuizLoginApi.STATE) state: String = ApiConstants.QuizLoginApi.STATE_SALUD_ANDALUCIA,
        @Query(ApiConstants.QuizLoginApi.REDIRECT_URI) redirect: String = ApiConstants.QuizLoginApi.REDIRECT_URI_VALUE
    ): Single<AuthorizeResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginStep1(
        @Query(ApiConstants.QuizLoginApi.ACTION) response: String = ApiConstants.QuizLoginApi.ACTION_LOGIN,
        @Query(ApiConstants.QuizLoginApi.LOGIN_METHOD) loginMethod: String = ApiConstants.QuizLoginApi.LOGIN_METHOD_DATOS_SMS,
        @Query(ApiConstants.QuizLoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.QuizLoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.QuizLoginApi.ID_TYPE) idType: String,
        @Query(ApiConstants.QuizLoginApi.NIF_NIE) identifier: String,
        @Query(ApiConstants.QuizLoginApi.NUHSA) nuhsa: String,
        @Query(ApiConstants.QuizLoginApi.BIRTHDAY) birthdate: String,
        @Query(ApiConstants.QuizLoginApi.PHONE_NUMBER) phoneNumber: String,
        @Query(ApiConstants.QuizLoginApi.STEP) step: String = ApiConstants.QuizLoginApi.STEP_1
    ): Single<LoginResponseData>

    @GET(URL_AUTHORIZE_LOGIN)
    fun loginStep2(
        @Query(ApiConstants.QuizLoginApi.ACTION) response: String = ApiConstants.QuizLoginApi.ACTION_LOGIN,
        @Query(ApiConstants.QuizLoginApi.LOGIN_METHOD) apiKey: String = ApiConstants.QuizLoginApi.LOGIN_METHOD_DATOS_SMS,
        @Query(ApiConstants.QuizLoginApi.SESSION_ID) sessionId: String,
        @Query(ApiConstants.QuizLoginApi.SESSION_DATA) sessionData: String,
        @Query(ApiConstants.QuizLoginApi.STEP) step: String = ApiConstants.QuizLoginApi.STEP_2,
        @Query(ApiConstants.QuizLoginApi.PIN_SMS) pin: String
    ): Single<LoginResponseData>
}
