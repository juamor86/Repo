package es.juntadeandalucia.msspa.saludandalucia.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicFeedbackController
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.DynamicUIBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.NavBackPressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsNotReadedCountUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveSharedDynamicIconsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetWalletIsActiveUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigationController
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.HmsTokenProvider
import es.juntadeandalucia.msspa.saludandalucia.presentation.splash.SplashContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.splash.SplashPresenter

@Module
class ActivityModule {

    @Provides
    fun providePresenter(
        getFirstAccessUseCase: GetFirstAccessUseCase,
        getNotificationsNotReadedCountUseCase: GetNotificationsNotReadedCountUseCase,
        sessionBus: SessionBus,
        dynamicUIBus: DynamicUIBus,
        navBackPressedBus: NavBackPressedBus,
        loginAdvicePressedBus: LoginAdvicePressedBus,
        saveFirstAccess: SetFirstAccessUseCase,
        launcherScreenBus: LauncherScreenBus,
        saveLastUpdateDynReleasesUseCase: SaveLastUpdateDynReleasesUseCase,
        getLastUpdateDynReleasesUseCase: GetLastUpdateDynReleasesUseCase
    ): MainContract.Presenter =
        MainPresenter(
            getFirstAccessUseCase,
            getNotificationsNotReadedCountUseCase,
            sessionBus,
            dynamicUIBus,
            navBackPressedBus,
            loginAdvicePressedBus,
            saveFirstAccess,
            launcherScreenBus,
            saveLastUpdateDynReleasesUseCase,
            getLastUpdateDynReleasesUseCase
        )

    @Provides
    fun provideSplashPresenter(): SplashContract.Presenter = SplashPresenter()

    @Provides
    fun provideDynamicNavigationController(
        dynamicUIBus: DynamicUIBus,
        saveSharedDynamicIconsUseCase: SaveSharedDynamicIconsUseCase,
        setWalletIsActiveUseCase: SetWalletIsActiveUseCase,
        sessionBus: SessionBus,
        getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase
        ): DynamicNavigationController =
        DynamicNavigationController(
            dynamicUIBus,
            saveSharedDynamicIconsUseCase,
            setWalletIsActiveUseCase,
            sessionBus,
            getNotificationsPhoneNumberUseCase
        )

    @Provides
    fun provideHmsTokenProvider(
        saveHmsGmsTokenUseCase: SaveHmsGmsTokenUseCase,
        getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
        updateNotificationsSubscriptionUseCase: UpdateNotificationsSubscriptionUseCase,
        getHmsGmsTokenUseCase: GetHmsGmsTokenUseCase
    ): HmsTokenProvider =
        HmsTokenProvider(saveHmsGmsTokenUseCase, getNotificationsPhoneNumberUseCase, updateNotificationsSubscriptionUseCase, getHmsGmsTokenUseCase)

    @Provides
    fun provideDynamicFeedbackController(
        getEventsUseCase: GetServiceEventsUseCase,
        getSavedLikeItUseCase: GetSavedLikeItUseCase,
        saveLikeItUseCase: SaveLikeItUseCase,
        executeJavascriptUseCase: ExecuteJavascriptUseCase
    ): DynamicFeedbackController = DynamicFeedbackController(
        getEventsUseCase, saveLikeItUseCase, getSavedLikeItUseCase, executeJavascriptUseCase
    )
}
