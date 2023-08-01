package es.juntadeandalucia.msspa.authentication.di.module

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.repository.mock.PreferencesRepositoryMockImpl
import es.juntadeandalucia.msspa.authentication.data.factory.repository.persistence.PreferencesRepositoryPreferencesImpl
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AuthLibraryModule(private val activity: BaseActivity) {

    @Provides
    @Singleton
    fun providePreferences(): SharedPreferences = activity.applicationContext.getSharedPreferences(
        ApiConstants.Preferences.PREF_NAME,
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideContext(): Context = activity.applicationContext

    @Provides
    @Singleton
    fun provideCryptography(): CrytographyManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PinPatternCryptographyManager()
        } else {
            null
        }

    @Provides
    @Singleton
    fun provideMsspaAuthenticationConfig(): MsspaAuthenticationConfig = activity.authConfig

    @Provides
    @Singleton
    fun providePreferencesFactory(sharedPreferences: SharedPreferences): PreferencesRepositoryFactory =
        PreferencesRepositoryFactory(
            PreferencesRepositoryMockImpl(), PreferencesRepositoryPreferencesImpl(sharedPreferences)
        )

    @Provides
    @Singleton
    fun provideNavBackPressedBehaviorSubject(
        navBackPressed: NavBackPressed
    ): NavBackPressedBus = NavBackPressedBus(navBackPressed)

    @Provides
    fun provideNavBackPressed(): NavBackPressed = activity.navBackPressed

    // endregion

}