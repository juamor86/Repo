package es.inteco.conanmobile.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import es.inteco.conanmobile.data.api.CONANApi
import es.inteco.conanmobile.data.utils.ApiConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetModule {

    @Provides
    fun provideGson(): Gson =
        GsonBuilder().disableHtmlEscaping().create()

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
            .hostnameVerifier { _, _ ->
                true
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    fun provideRetrofit(gson: Gson, @Named("okHttpClient") okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(ApiConstants.General.API_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Named("interceptor")
    @Provides
    fun provideInterceptor(): Interceptor = Interceptor {
        with(it) {
            val requestBuilder = request()
                .newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Android")
                .addHeader(
                    ApiConstants.General.CONTENT_TYPE_HEADER,
                    ApiConstants.General.APPLICATION_JSON
                )
                .addHeader(
                    ApiConstants.General.ACCEPT_HEADER,
                    ApiConstants.General.APPLICATION_JSON
                )

            proceed(requestBuilder.build())
        }
    }

    @Provides
    fun provideCONANApiService(retrofit: Retrofit): CONANApi = retrofit.create(CONANApi::class.java)
}