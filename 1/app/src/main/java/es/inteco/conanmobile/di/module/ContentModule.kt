package es.inteco.conanmobile.di.module

import dagger.Module
import dagger.Provides
import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.usecases.*
import es.inteco.conanmobile.domain.usecases.analisys.GetMaliciousAPKUseCase
import es.inteco.conanmobile.domain.usecases.analisys.GetMaliciousAppUseCase
import es.inteco.conanmobile.domain.usecases.analisys.PostAnalysisResultUseCase

@Module
class ContentModule {

    //region - Use cases
    @Provides
    fun provideRegisterDeviceUseCase(configurationRepositoryFactory: ConfigurationRepositoryFactory): RegisterDeviceUseCase =
        RegisterDeviceUseCase(configurationRepositoryFactory)

    @Provides
    fun provideGetConfigurationUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory,
        configurationRepositoryFactory: ConfigurationRepositoryFactory
    ): GetConfigurationUseCase =
        GetConfigurationUseCase(preferencesRepositoryFactory, configurationRepositoryFactory)

    @Provides
    fun provideGetDefaultAnalysisUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetDefaultAnalysisUseCase =
        GetDefaultAnalysisUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSetFirstAccessUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetFirstAccessUseCase =
        SetFirstAccessUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetFirstAccessUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetFirstAccessUseCase =
        GetFirstAccessUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetDeviceAnalysisUseCase(analysisRepositoryFactory: AnalysisRepositoryFactory): ExistLastAnalysisUseCase =
        ExistLastAnalysisUseCase(analysisRepositoryFactory)

    @Provides
    fun provideGetMaliciousAppUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory,
        incibeRepositoryFactory: IncibeRepositoryFactory
    ): GetMaliciousAppUseCase =
        GetMaliciousAppUseCase(preferencesRepositoryFactory, incibeRepositoryFactory)

    @Provides
    fun provideGetMaliciousAPKUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory,
        incibeRepositoryFactory: IncibeRepositoryFactory
    ): GetMaliciousAPKUseCase =
        GetMaliciousAPKUseCase(preferencesRepositoryFactory, incibeRepositoryFactory)

    @Provides
    fun provideSaveDeviceAnalysisUseCase(analysisRepositoryFactory: AnalysisRepositoryFactory): SaveAnalysisUseCase =
        SaveAnalysisUseCase(analysisRepositoryFactory)

    @Provides
    fun provideGetLastAnalysisUseCase(analysisRepositoryFactory: AnalysisRepositoryFactory): GetLastAnalysisUseCase =
        GetLastAnalysisUseCase(analysisRepositoryFactory)

    @Provides
    fun provideSaveDefaultAnalysisUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveDefaultAnalysisUseCase =
        SaveDefaultAnalysisUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetAnalysisLaunchedUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetAnalysisLaunchedUseCase =
        GetAnalysisLaunchedUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSetAnalysisLaunchedUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetAnalysisLaunchedUseCase =
        SetAnalysisLaunchedUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetNextAnalysisDateTimeUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetNextAnalysisDateTimeUseCase =
        GetNextAnalysisDateTimeUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSetNextAnalysisDateTimeUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetNextAnalysisDateTimeUseCase =
        SetNextAnalysisDateTimeUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetIpBotnetUseCase(incibeRepositoryFactory: IncibeRepositoryFactory): GetIpBotnetUseCase =
        GetIpBotnetUseCase(incibeRepositoryFactory)

    @Provides
    fun provideGetFirstAnalysisLaunchedUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetFirstAnalysisLaunchedUseCase =
        GetFirstAnalysisLaunchedUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSetFirstAnalysisLaunchedUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetFirstAnalysisLaunchedUseCase =
        SetFirstAnalysisLaunchedUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetWarningsUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory,
        incibeRepositoryFactory: IncibeRepositoryFactory
    ): GetWarningsUseCase =
        GetWarningsUseCase(preferencesRepositoryFactory, incibeRepositoryFactory)

    @Provides
    fun providePostAnalysisResultUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory,
        incibeRepositoryFactory: IncibeRepositoryFactory
    ): PostAnalysisResultUseCase =
        PostAnalysisResultUseCase(preferencesRepositoryFactory, incibeRepositoryFactory)

    @Provides
    fun provideCheckPendingWarningsUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory,
        incibeRepositoryFactory: IncibeRepositoryFactory
    ): CheckPendingWarningsUseCase =
        CheckPendingWarningsUseCase(preferencesRepositoryFactory, incibeRepositoryFactory)

    @Provides
    fun provideGetLastAlertAnalysisUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetLastAlertAnalysisUseCase =
        GetLastAlertAnalysisUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSaveLastAlertAnalysisUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveLastAlertAnalysisUseCase =
        SaveLastAlertAnalysisUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideConfigurationUseCase(configurationRepositoryFactory: ConfigurationRepositoryFactory): SaveConfigurationUseCase =
        SaveConfigurationUseCase(configurationRepositoryFactory)

    @Provides
    fun provideGetConfigurationPreferencesUseCase(configurationRepositoryFactory: ConfigurationRepositoryFactory): GetConfigPreferencesUseCase =
        GetConfigPreferencesUseCase(configurationRepositoryFactory)


    //endregion
}