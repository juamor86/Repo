package es.inteco.conanmobile.di.module

import dagger.Module
import dagger.Provides
import es.inteco.conanmobile.domain.usecases.*
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import es.inteco.conanmobile.presentation.main.MainContract
import es.inteco.conanmobile.presentation.main.MainPresenter
import es.inteco.conanmobile.presentation.splash.SplashContract
import es.inteco.conanmobile.presentation.splash.SplashPresenter

@Module
class ActivityModule {

    @Provides
    fun providePresenter(
        getConfigurationUseCase: GetConfigurationUseCase,
        getDefaultAnalysisUseCase: GetDefaultAnalysisUseCase,
        existLastAnalysisUseCase: ExistLastAnalysisUseCase,
        getLastAnalysisUseCase: GetLastAnalysisUseCase,
        analysisController: AnalysisController,
        checkPendingWarningsUseCase: CheckPendingWarningsUseCase,
        getLastAlertAnalysisUseCase: GetLastAlertAnalysisUseCase,
        saveLastAlertAnalysisUseCase: SaveLastAlertAnalysisUseCase
    ): MainContract.Presenter = MainPresenter(
        getConfigurationUseCase,
        getDefaultAnalysisUseCase,
        existLastAnalysisUseCase,
        getLastAnalysisUseCase,
        analysisController,
        checkPendingWarningsUseCase,
        getLastAlertAnalysisUseCase,
        saveLastAlertAnalysisUseCase
    )

    @Provides
    fun provideSplashPresenter(
        getConfigurationUseCase: GetConfigurationUseCase,
        setFirstAccessUseCase: SetFirstAccessUseCase,
        getFirstAccessUseCase: GetFirstAccessUseCase,
        saveDefaultAnalysisUseCase: SaveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase: GetDefaultAnalysisUseCase,
        registerDeviceUseCase: RegisterDeviceUseCase,
        saveDeviceRegisterUseCase: SaveRegisteredDeviceUseCase,
        saveConfigurationUseCase: SaveConfigurationUseCase
    ): SplashContract.Presenter = SplashPresenter(
        getConfigurationUseCase,
        setFirstAccessUseCase,
        getFirstAccessUseCase,
        saveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase,
        registerDeviceUseCase,
        saveDeviceRegisterUseCase,
        saveConfigurationUseCase
    )
}