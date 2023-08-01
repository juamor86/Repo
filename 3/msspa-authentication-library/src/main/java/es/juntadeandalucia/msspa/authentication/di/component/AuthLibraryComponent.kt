package es.juntadeandalucia.msspa.authentication.di.component

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.di.module.ActivityModule
import es.juntadeandalucia.msspa.authentication.di.module.AuthLibraryModule
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthLibraryModule::class, ActivityModule::class])
internal interface AuthLibraryComponent {
    val context: Context

    val preferences: SharedPreferences

    val preferencesRepositoryFactory: PreferencesRepositoryFactory

    val cryptographyManager: CrytographyManager?

    val msspaAuthenticationConfig: MsspaAuthenticationConfig

    val navBackPressedBus: NavBackPressedBus
}