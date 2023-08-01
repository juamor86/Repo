package es.juntadeandalucia.msspa.authentication.di.module

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaTokenManager
import es.juntadeandalucia.msspa.authentication.data.factory.AuthorizeRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.api.MSSPAAuthorizeApi
import es.juntadeandalucia.msspa.authentication.data.factory.api.MSSPALoginApi
import es.juntadeandalucia.msspa.authentication.data.factory.entities.MSSPAResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.repository.mock.AuthorizeRepositoryMockImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.mock.LoginMockImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.network.AuthorizeRepositoryNetworkImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.network.LoginImpl
import es.juntadeandalucia.msspa.authentication.domain.usecases.InvalidateTokenUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.RefreshTokenUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.ValidateTokenUseCase
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.exceptions.*
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetModule(private val authenticationConfig: MsspaAuthenticationConfig) {

    @Provides
    fun provideValidateTokenUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): ValidateTokenUseCase = ValidateTokenUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun provideInvalidateTokenUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): InvalidateTokenUseCase = InvalidateTokenUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun provideRefreshTokenUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): RefreshTokenUseCase = RefreshTokenUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun provideMsspaTokenManager(
        validateTokenUseCase: ValidateTokenUseCase,
        invalidateTokenUseCase: InvalidateTokenUseCase,
        refreshTokenUseCase: RefreshTokenUseCase
    ): MsspaTokenManager =
        MsspaTokenManager(
            validateTokenUseCase,
            invalidateTokenUseCase,
            refreshTokenUseCase
        )

    @Provides
    fun provideAuthorizeFactory(msspaAuthorizeApi: MSSPAAuthorizeApi): AuthorizeRepositoryFactory =
        AuthorizeRepositoryFactory(
            AuthorizeRepositoryMockImpl(),
            AuthorizeRepositoryNetworkImpl(msspaAuthorizeApi)
        )

    @Provides
    fun provideLoginPersonalDataRepositoryFactory(msspaLoginApi: MSSPALoginApi, retrofit: Retrofit,
                                                  @Named("loginOkHttpClient") okHttpClient: OkHttpClient
    ): LoginPersonalDataRepositoryFactory =
        LoginPersonalDataRepositoryFactory(
            LoginMockImpl(),
            LoginImpl(msspaLoginApi, okHttpClient, retrofit)
        )

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

    @Named("authorizeOkHttpClient")
    @Provides
    fun provideAuthorizeOkHttpClient(
        @Named("interceptor") interceptor: Interceptor,
        @Named("loginInterceptor") loginInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(
                ApiConstants.General.HTTP_CONNECT_TIMEOUT,
                TimeUnit.MILLISECONDS
            )
            .followRedirects(false)
            .readTimeout(ApiConstants.General.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loginInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    fun provideRetrofit(
        gson: Gson,
        @Named("okHttpClient") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(authenticationConfig.environment.url)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Named("interceptor")
    @Provides
    fun provideInterceptor(
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
                    authenticationConfig.appKey
                )
                .addHeader(
                    ApiConstants.General.API_KEY_HEADER,
                    authenticationConfig.apiKey
                )
            val resp = proceed(requestBuilder.build())

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
                    } else if (msspaResponse.errorDescription?.contains(ApiConstants.General.QR_LOGIN_ERROR) == true) {
                        throw QRLoginNotFoundException()
                    } else if (msspaResponse.errorDescription?.contains(ApiConstants.General.WRONG_DATA_QR) == true) {
                        throw QRLoginExpiredException()
                    } else if (msspaResponse.error == MSSPAResponseData.LOGIN_REQUIRED) {
                        throw LoginRequiredException()
                    }
                    resp
                }
                else -> {
                    if (resp.headers[ApiConstants.General.LOCATION]?.contains(ApiConstants.General.STATUS_ERROR) == true) {
                        resp.headers[ApiConstants.General.LOCATION].toString()
                            .split("&")[2].let { errorMsg ->
                            when {
                                errorMsg.contains(ApiConstants.General.INVALID_BDU_DATA) -> {
                                    throw LoginRequiredException()
                                }
                                errorMsg.contains(ApiConstants.General.INVALID_BDU_PHONE) -> {
                                    throw LoginPhoneRequiredException()
                                }
                                errorMsg.contains(ApiConstants.General.MAX_SMS_ATTEMPS) -> {
                                    throw VerificationMaxAttemptsExceededException()
                                }
                                errorMsg.contains(ApiConstants.General.INVALID_PIN_SMS) -> {
                                    throw InvalidPINSMSReceivedException()
                                }
                                errorMsg.contains(ApiConstants.General.PROTECTED_USER) -> {
                                    throw ProtectedUserException()
                                }
                                errorMsg.contains(ApiConstants.General.NOT_PRIVATE_HEALTH_CARE_USER) -> {
                                    throw NotPrivateHealthCareException()
                                }
                                errorMsg.contains(ApiConstants.General.QR_LOGIN_ERROR) -> {
                                    throw QRLoginNotFoundException()
                                }
                                errorMsg.contains(ApiConstants.General.WRONG_DATA_QR) -> {
                                    throw QRLoginExpiredException()
                                }
                                errorMsg.contains(ApiConstants.General.SEND_NOTIFICATION_ERROR) -> {
                                    throw SendNotificationErrorException()
                                }
                                errorMsg.contains(ApiConstants.General.INVALID_LOGIN_REPEATED) -> {
                                    throw LoginInvalidRepeatedException()
                                }
                                else -> {
                                    throw LoginInvalidRequestException()
                                }
                            }
                        }
                    } else {
                        resp
                    }
                }
            }
        }
    }

    @Named("loginInterceptor")
    @Provides
    fun provideLoginHeaderInterceptor(): Interceptor = Interceptor {
        with(it) {
            val resp = it.proceed(it.request())
            if (resp.isRedirect) {
                val location = resp.header(ApiConstants.General.LOCATION, "")
                val uri = Uri.parse(location)
                var jsonStr = ""
                try {
                    val json = JSONObject()
                    json.put("sessionId", uri.getQueryParameter(ApiConstants.LoginApi.SESSION_ID))
                    json.put("sessionData", uri.getQueryParameter(ApiConstants.LoginApi.SESSION_DATA))
                    json.put("access_token", uri.getQueryParameter(ApiConstants.LoginApi.ACCESS_TOKEN))
                    json.put("token_type", uri.getQueryParameter(ApiConstants.LoginApi.TOKEN_TYPE))
                    json.put("expires_in", uri.getQueryParameter(ApiConstants.LoginApi.EXPIRES_IN))
                    json.put("scope", uri.getQueryParameter(ApiConstants.LoginApi.SCOPE))
                    json.put("totp_secret_key", uri.getQueryParameter(ApiConstants.LoginApi.TOTP_SECRET_KEY))
                    json.put("refresh_token", uri.getQueryParameter(ApiConstants.LoginApi.REFRESH_TOKEN))
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
    fun provideMSSPALoginApiService(
        retrofit: Retrofit,
        @Named("loginOkHttpClient") okHttpClient: OkHttpClient
    ): MSSPALoginApi =
        retrofit.newBuilder().client(okHttpClient).build().create(MSSPALoginApi::class.java)

    @Provides
    fun provideMSSPAAuthorizeApiService(
        retrofit: Retrofit,
        @Named("authorizeOkHttpClient") okHttpClient: OkHttpClient
    ): MSSPAAuthorizeApi =
        retrofit.newBuilder().client(okHttpClient).build().create(MSSPAAuthorizeApi::class.java)
}