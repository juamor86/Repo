package es.inteco.conanmobile.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import es.inteco.conanmobile.data.api.CONANApi
import es.inteco.conanmobile.data.factory.*
import es.inteco.conanmobile.data.repository.mock.*
import es.inteco.conanmobile.data.repository.network.ConfigurationRepositoryNetworkImpl
import es.inteco.conanmobile.data.repository.network.DefaultAnalysisRepositoryNetworkImpl
import es.inteco.conanmobile.data.repository.network.IncibeRepositoryNetworkImpl
import es.inteco.conanmobile.data.repository.preferences.AnalysisRepositoryPreferencesImpl
import es.inteco.conanmobile.data.repository.preferences.ConfigurationRepositoryPreferencesImpl
import es.inteco.conanmobile.data.repository.preferences.PreferencesRepositoryPreferencesImpl
import es.inteco.conanmobile.device.notification.NotificationManager
import es.inteco.conanmobile.device.notification.NotificationManagerImpl
import es.inteco.conanmobile.di.scope.PerApplication
import es.inteco.conanmobile.domain.base.bus.LifecycleBus
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import es.inteco.conanmobile.utils.Consts
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: App) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application = baseApp

    @Provides
    fun provideContext(): Context = baseApp.applicationContext

    @Provides
    @Singleton
    fun provideLifecycleBus(): LifecycleBus = LifecycleBus()

    @Provides
    @Singleton
    fun provideMasterKey(): MasterKey =
        MasterKey.Builder(baseApp.applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    @Provides
    @Singleton
    fun provideEncryptedPreferences(masterKey: MasterKey): SharedPreferences =
        EncryptedSharedPreferences.create(
            baseApp.applicationContext,
            Consts.MASTER_KEY_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    //region - Repository
    @Provides
    @Singleton
    fun providePreferencesFactory(sharedPreferences: SharedPreferences): PreferencesRepositoryFactory =
        PreferencesRepositoryFactory(
            PreferencesRepositoryMockImpl(), PreferencesRepositoryPreferencesImpl(sharedPreferences)
        )

    @Provides
    @Singleton
    fun provideConfigurationRepositoryFactory(
        context: Context, conanApi: CONANApi, sharedPreferences: SharedPreferences
    ): ConfigurationRepositoryFactory = ConfigurationRepositoryFactory(
        ConfigurationRepositoryMockImpl(context),
        ConfigurationRepositoryNetworkImpl(conanApi),
        ConfigurationRepositoryPreferencesImpl(sharedPreferences)
    )

    @Provides
    @Singleton
    fun provideDefaultAnalysisRepositoryFactory(
        context: Context, conanApi: CONANApi
    ): DefaultAnalysisRepositoryFactory = DefaultAnalysisRepositoryFactory(
        DefaultAnalysisRepositoryMockImpl(context), DefaultAnalysisRepositoryNetworkImpl(conanApi)
    )

    @Provides
    @Singleton
    fun provideIncibeRepositoryFactory(
        context: Context, conanApi: CONANApi
    ): IncibeRepositoryFactory = IncibeRepositoryFactory(
        IncibeRepositoryNetworkImpl(conanApi), IncibeRepositoryMockImpl(context)
    )

    @Provides
    @Singleton
    fun provideAnalysisController(
        context: Context
    ): AnalysisController = AnalysisController(
        context
    )

    @Provides
    @Singleton
    fun provideAnalysisRepositoryFactory(
        sharedPreferences: SharedPreferences
    ): AnalysisRepositoryFactory = AnalysisRepositoryFactory(
        AnalysisRepositoryMockImpl(), AnalysisRepositoryPreferencesImpl(sharedPreferences)
    )

    @Provides
    @Singleton
    fun provideNotificationManager(
        context: Context
    ): NotificationManager = NotificationManagerImpl(
        context
    )
    //endregion
}