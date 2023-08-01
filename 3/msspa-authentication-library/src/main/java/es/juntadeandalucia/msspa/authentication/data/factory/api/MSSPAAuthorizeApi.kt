package es.juntadeandalucia.msspa.authentication.data.factory.api

import es.juntadeandalucia.msspa.authentication.data.factory.entities.*
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MSSPAAuthorizeApi {
    companion object {
        private const val URL_AUTHORIZE = "${ApiConstants.General.URL_API_V1}/oauth2/authorize"
    }

    @GET(URL_AUTHORIZE)
    fun authorize(
        @Query(ApiConstants.LoginApi.RESPONSE_TYPE) response: String = ApiConstants.LoginApi.RESPONSE_TYPE_CODE,
        @Query(ApiConstants.LoginApi.CLIENT_ID) clientId: String,
        @Query(ApiConstants.LoginApi.SCOPE) scope: String = ApiConstants.LoginApi.SCOPE_CIUDADANO,
        @Query(ApiConstants.LoginApi.REDIRECT_URI) redirect: String
    ): Single<AuthorizeResponseData>
}