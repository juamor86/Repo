package com.example.msspa_megusta_library.di.module

import android.net.Uri
import com.example.msspa_megusta_library.data.api.MeGustaApi
import com.example.msspa_megusta_library.utils.ApiConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
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
class NetModule() {


    /*@Provides
    fun provideMsspaTokenManager(
        validateTokenUseCase: ValidateTokenUseCase,
        invalidateTokenUseCase: InvalidateTokenUseCase,
        refreshTokenUseCase: RefreshTokenUseCase
    ): MsspaTokenManager =
        MsspaTokenManager(
            validateTokenUseCase,
            invalidateTokenUseCase,
            refreshTokenUseCase
        )*/

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


    //TODO baseurl change dinamically
    @Provides
    fun provideRetrofit(
        gson: Gson,
        @Named("okHttpClient") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://ws237.sspa.juntadeandalucia.es:9443")
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
                    "msspa.app.102"
                )
                .addHeader(
                    ApiConstants.General.API_KEY_HEADER,
                    "l7460a9e8f1e594654ac37ac53fd2ee8e2"
                )
             proceed(requestBuilder.build())
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
                    json.put("sessionId", uri.getQueryParameter(ApiConstants.Api.SESSION_ID))
                    json.put("sessionData", uri.getQueryParameter(ApiConstants.Api.SESSION_DATA))
                    json.put("access_token", uri.getQueryParameter(ApiConstants.Api.ACCESS_TOKEN))
                    json.put("token_type", uri.getQueryParameter(ApiConstants.Api.TOKEN_TYPE))
                    json.put("expires_in", uri.getQueryParameter(ApiConstants.Api.EXPIRES_IN))
                    json.put("scope", uri.getQueryParameter(ApiConstants.Api.SCOPE))
                    json.put("totp_secret_key", uri.getQueryParameter(ApiConstants.Api.TOTP_SECRET_KEY))
                    json.put("refresh_token", uri.getQueryParameter(ApiConstants.Api.REFRESH_TOKEN))
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
    fun provideMeGustaApiService(
        retrofit: Retrofit,
        @Named("loginOkHttpClient") okHttpClient: OkHttpClient
    ): MeGustaApi =
        retrofit.newBuilder().client(okHttpClient).build().create(MeGustaApi::class.java)
}