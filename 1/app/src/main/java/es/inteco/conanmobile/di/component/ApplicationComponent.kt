package es.inteco.conanmobile.di.component

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import es.inteco.conanmobile.data.api.CONANApi
import es.inteco.conanmobile.data.factory.*
import es.inteco.conanmobile.device.notification.NotificationManager
import es.inteco.conanmobile.di.module.ActivityModule
import es.inteco.conanmobile.di.module.ApplicationModule
import es.inteco.conanmobile.di.module.ContentModule
import es.inteco.conanmobile.di.module.NetModule
import es.inteco.conanmobile.domain.base.bus.LifecycleBus
import es.inteco.conanmobile.domain.usecases.GetIpBotnetUseCase
import es.inteco.conanmobile.domain.usecases.analisys.CheckDeviceIPUseCase
import es.inteco.conanmobile.domain.usecases.analisys.HostFileExistUseCase
import es.inteco.conanmobile.domain.usecases.analisys.ListAppsUseCase
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, ActivityModule::class, NetModule::class, ContentModule::class]
)
interface ApplicationComponent {

    //region - Expose sub-graphs dependencies
    val preferences: SharedPreferences

    val context: Context

    val conanApi: CONANApi

    val configurationRepositoryFactory: ConfigurationRepositoryFactory

    val defaultAnalysisRepositoryFactory: DefaultAnalysisRepositoryFactory

    val preferencesRepositoryFactory: PreferencesRepositoryFactory

    val analysisRepositoryFactory: AnalysisRepositoryFactory

    val incibeRepositoryFactory: IncibeRepositoryFactory

    val analysisController: AnalysisController

    val notificationManager: NotificationManager

    val getIpBotnetUseCase: GetIpBotnetUseCase

    val lifecycleBus: LifecycleBus

    fun inject(checkDeviceIPUseCase: CheckDeviceIPUseCase)

    fun inject(listAppsUseCase: ListAppsUseCase)

    fun inject(hostFileExistUseCase: HostFileExistUseCase)
    //endregion

    fun inject(application: App)
}