package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPALoginApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPAResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.*
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.FORBIDDEN_QUESTIONNAIRE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.FULL_URL_CONSTS
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Provider

@Module
class NetModule {

    @Provides
    fun provideGson(): Gson =
        GsonBuilder().create()

    @Named("okHttpClient")
    @Provides
    fun provideOkHttpClient(
        @Named("interceptor") interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(
                ApiConstants.General.HTTP_CONNECT_TIMEOUT,
                TimeUnit.MILLISECONDS
            )
            .readTimeout(ApiConstants.General.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Named("loginOkHttpClient")
    @Provides
    fun provideLoginOkHttpClient(
        @Named("interceptor") interceptor: Interceptor,
        @Named("loginInterceptor") loginInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(
                ApiConstants.General.HTTP_CONNECT_TIMEOUT,
                TimeUnit.MILLISECONDS
            )
            .readTimeout(ApiConstants.General.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loginInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    fun provideRetrofit(gson: Gson, @Named("okHttpClient") okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(ApiConstants.General.API_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Named("interceptor")
    @Provides
    fun provideInterceptor(
        sessionBus: Lazy<SessionBus>,
        session: Provider<Session>,
        gson: Gson
    ): Interceptor = Interceptor {
        with(it) {
            val requestBuilder = request()
                .newBuilder()
                .addHeader(
                    ApiConstants.General.CONTENT_TYPE_HEADER,
                    ApiConstants.General.APPLICATION_JSON
                )
                .addHeader(
                    ApiConstants.General.ACCEPT_HEADER,
                    ApiConstants.General.APPLICATION_JSON
                )
                .addHeader(ApiConstants.General.ID_DEVICE_HEADER, ApiConstants.General.DEFAULT)
                .addHeader(
                    ApiConstants.General.ID_DEVICE_MSSPA_HEADER,
                    ApiConstants.General.DEFAULT
                )
                .addHeader(
                    ApiConstants.General.APP_KEY_HEADER,
                    ApiConstants.General.SALUD_ANDALUCIA_APP_KEY_IDENTIFICATION
                )
                .addHeader(
                    ApiConstants.General.API_KEY_HEADER,
                    ApiConstants.General.SALUD_ANDALUCIA_API_KEY_IDENTIFICATION
                )

            var request = requestBuilder.build()
            if (request.headers[ApiConstants.General.AUTHORIZATION_HEADER] == null) {
                session.get().msspaAuthenticationEntity.authorizationToken?.let { authorizationToken ->
                    request = request.newBuilder()
                        .addHeader(ApiConstants.General.AUTHORIZATION_HEADER, authorizationToken)
                        .build()
                }
            }
            val resp = proceed(request)
            when (resp.code) {
                ApiConstants.ErrorCode.TOO_MANY_REQUEST_ERROR_CODE -> {
                    throw TooManyRequestException()
                }
                ApiConstants.ErrorCode.DEFAULT_ERROR_CODE -> {
                    val msspaResponse =
                        gson.fromJson(resp.body?.charStream(), MSSPAResponseData::class.java)
                    if (msspaResponse.issueErrorCode == MSSPAResponseData.MAX_ATTEMPTS_EXCEEDED ||
                        msspaResponse.error == MSSPAResponseData.MAX_ATTEMPTS
                    ) {
                        throw VerificationMaxAttemptsExceededException()
                    } else if (msspaResponse.error == MSSPAResponseData.LOGIN_REQUIRED) {
                        throw LoginRequiredException()
                    }
                    resp
                }
                ApiConstants.ErrorCode.NO_CERTIFICATE_ERROR_CODE -> {
                    val msspaResponse = gson.fromJson(resp.body?.charStream(), MSSPAResponseData::class.java)
                    if (msspaResponse.issueDetailsText == MSSPAResponseData.NO_MONITORING_PROGRAM) {
                        throw NoMonitoringProgramException()
                    } else if (isRequestAdviceMethodPut(request)) {
                        throw NotExistAdviceException()
                    } else {
                        throw NoCertificateException()
                    }
                }
                ApiConstants.ErrorCode.FORBIDDEN_ERROR_CODE -> {
                    val msspaResponse =
                        gson.fromJson(resp.body?.charStream(), MSSPAResponseData::class.java)
                    if (msspaResponse.issueErrorCode == MSSPAResponseData.FORBIDDEN &&
                        msspaResponse.issueDetailsText.equals(MSSPAResponseData.UNDER_16)
                    ) {
                        throw Under16LoginException()
                    } else if(msspaResponse.issueDiagnostic == FORBIDDEN_QUESTIONNAIRE) {
                        throw NoAuthorizedQuestionnaire()
                    }
                    resp
                }
                ApiConstants.ErrorCode.UNAUTHORIZED -> {
                    sessionBus.get().onUnauthorizedEvent()
                    resp
                }
                else -> {
                    if (resp.headers[ApiConstants.General.HEADER_LOCATION]?.contains(ApiConstants.General.STATUS_ERROR) == true) {
                        manageErrorResponse(resp)
                    } else
                        resp
                }
            }
        }
    }

    private fun manageErrorResponse(resp: Response): Response {
        val errorContent = Uri.parse(resp.headers[ApiConstants.General.HEADER_LOCATION])
            .getQueryParameter(ApiConstants.General.QUERY_PARAM_ERROR_DESCRIPTION)

        return when (errorContent) {
            ApiConstants.ErrorContentDescription.INVALID_BDU_DATA -> throw InvalidBDUData()
            ApiConstants.ErrorContentDescription.MAX_ATTEMPTS-> throw VerificationMaxAttemptsExceededException()
            ApiConstants.ErrorContentDescription.PROTECTED_USER-> throw ProtectedUserException()
            else -> resp
        }
    }

    @Named("loginInterceptor")
    @Provides
    fun provideLoginHeaderInterceptor(): Interceptor = Interceptor {
        with(it) {
            val resp = it.proceed(it.request())
            if (resp.isRedirect) {
                val location = resp.header("Location", "")
                val uri = Uri.parse(location)
                var jsonStr: String = ""
                try {
                    var json = JSONObject()
                    json.put("sessionId", uri.getQueryParameter("sessionID"))
                    json.put("sessionData", uri.getQueryParameter("sessionData"))
                    jsonStr = json.toString()
                } catch (ex: Exception) {
                }

                val contentType: MediaType? = resp.body?.contentType()
                resp.newBuilder().body(jsonStr.toResponseBody(contentType)).code(200).build()
            } else {
                resp
            }
        }
    }

    @Provides
    fun provideMSSPAApiService(retrofit: Retrofit): MSSPAApi = retrofit.create(MSSPAApi::class.java)

    @Provides
    fun provideMSSPALoginApiService(
        retrofit: Retrofit,
        @Named("loginOkHttpClient") okHttpClient: OkHttpClient
    ): MSSPALoginApi =
        retrofit.newBuilder().client(okHttpClient).build().create(MSSPALoginApi::class.java)

    private fun isRequestAdviceMethodPut(request: Request): Boolean {
        return request.method.equals(ApiConstants.Advices.PUT) && request.url.encodedPath.contains(FULL_URL_CONSTS)
    }
}
