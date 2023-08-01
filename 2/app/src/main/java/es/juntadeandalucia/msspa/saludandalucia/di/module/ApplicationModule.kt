package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPALoginApi
import es.juntadeandalucia.msspa.saludandalucia.data.factory.*
import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.KeyValueRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.CovidCertificateDao
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.NotificationDao
import es.juntadeandalucia.msspa.saludandalucia.data.repository.file.DynamicUIRepositoryFileImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.json.KeyValueRepositoryJsonImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.*
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.*
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AnnouncementsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AppointmentsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AppsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.AuthorizeRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.CovidCertificateRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.DynamicUIRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.FeaturedRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.LoginRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.MonitoringRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.NewsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.NotificationsRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.NotificationsSubscriptionRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.PreferencesRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.QuizRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.UserRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AnnouncementsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AppointmentsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AppsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.AuthorizeRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.DynamicUIRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.FeaturedRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.LoginRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.MonitoringRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.NewsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.NotificationsRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.NotificationsSubscriptionRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.PreferencesRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.QuizRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.UserRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence.CovidCertificateRepositoryDataBaseImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence.NotificationsRepositoryDataBaseImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.preferences.PreferencesRepositoryPreferencesImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.preferences.UserRepositoryPreferencesImpl
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerApplication
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.LoginAdvicePressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.NavBackPressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.LauncherScreenBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SmsBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LoginAdvicePressed
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LauncherScreen
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetUserSessionUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.RemoveSessionUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveSessionUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: App) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application = baseApp

    @Provides
    @Singleton
    fun providePreferences(): SharedPreferences =
        baseApp.getSharedPreferences(Consts.PREF_NAME, MODE_PRIVATE)

    @Provides
    fun provideContext(): Context = baseApp.applicationContext

    @Provides
    fun provideQuizSession(): QuizSession? = App.quizSession

    @Provides
    fun provideSession(): Session = App.session

    @Provides
    fun provideNavBackPressed(): NavBackPressed = App.navBackPressed

    @Provides
    fun provideLoginAdvicePressed(): LoginAdvicePressed = App.loginAdvicePressed

    @Provides
    fun provideLauncherScreen(): LauncherScreen = App.launcherScreen

    @Provides
    @Singleton
    fun provideCryptography(): CrytographyManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PinPatternCryptographyManager()
        } else {
            null
        }

    //region - Repository
    @Provides
    @Singleton
    fun provideAnnouncementRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): AnnouncementsRepositoryFactory = AnnouncementsRepositoryFactory(
        AnnouncementsRepositoryMockImpl(context), AnnouncementsRepositoryNetworkImpl(msspaApi)
    )

    @Provides
    @Singleton
    fun provideAppsRepositoryFactory(context: Context, msspaApi: MSSPAApi): AppsRepositoryFactory =
        AppsRepositoryFactory(
            AppsRepositoryMockImpl(context), AppsRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideNewsRepositoryFactory(context: Context, msspaApi: MSSPAApi): NewsRepositoryFactory =
        NewsRepositoryFactory(
            NewsRepositoryMockImpl(context), NewsRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideFeaturedRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): FeaturedRepositoryFactory = FeaturedRepositoryFactory(
        FeaturedRepositoryMockImpl(context), FeaturedRepositoryNetworkImpl(msspaApi)
    )

    @Provides
    @Singleton
    fun providePreferencesFactory(
        sharedPreferences: SharedPreferences,
        msspaApi: MSSPAApi
    ): PreferencesRepositoryFactory =
        PreferencesRepositoryFactory(
            PreferencesRepositoryMockImpl(),
            PreferencesRepositoryPreferencesImpl(sharedPreferences),
            PreferencesRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideFeedbackFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): FeedbackRepositoryFactory =
        FeedbackRepositoryFactory(
            LikeItRepositoryMockImpl(context),
            LikeItRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideLoginFactory(msspaLoginApi: MSSPALoginApi): LoginRepositoryFactory =
        LoginRepositoryFactory(
            LoginRepositoryMockImpl(),
            LoginRepositoryNetworkImpl(msspaLoginApi)
        )

    @Provides
    @Singleton
    fun provideAuthorizeFactory(msspaLoginApi: MSSPALoginApi): AuthorizeRepositoryFactory =
        AuthorizeRepositoryFactory(
            AuthorizeRepositoryMockImpl(),
            AuthorizeRepositoryNetworkImpl(msspaLoginApi)
        )

    @Provides
    @Singleton
    fun provideQuizRepositoryFactory(
        msspaApi: MSSPAApi
    ): QuizRepositoryFactory =
        QuizRepositoryFactory(
            QuizRepositoryMockImpl(), QuizRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideDynamicQuestionnairesRepositoryFactory(
        msspaApi: MSSPAApi
    ): DynamicQuestionnairesRepositoryFactory =
        DynamicQuestionnairesRepositoryFactory(
            DynamicQuestionnairesRepositoryMockImpl(),
            DynamicQuestionnairesRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideUserRepositoryFactory(
        msspaApi: MSSPAApi,
        context: Context
    ): UserRepositoryFactory =
        UserRepositoryFactory(
            UserRepositoryMockImpl(context),
            UserRepositoryPreferencesImpl(),
            UserRepositoryNetworkImpl(msspaApi)
        )

    @Provides
    @Singleton
    fun provideKeyValueRepositoryFactory(
        context: Context
    ): KeyValueRepositoryFactory = KeyValueRepositoryFactory(
        KeyValueRepositoryJsonImpl(context)
    )

    @Provides
    @Singleton
    fun provideNotificationsRepositoryFactory(
        context: Context,
        notificationDao: NotificationDao,
        msspaApi: MSSPAApi
    ): NotificationsRepositoryFactory =
        NotificationsRepositoryFactory(
            NotificationsRepositoryDataBaseImpl(notificationDao),
            NotificationsRepositoryNetworkImpl(msspaApi),
            NotificationsRepositoryMockImpl(context, notificationDao)

        )

    @Provides
    @Singleton
    fun provideCovidCertificateRepositoryFactory(
        context: Context,
        certificateDao: CovidCertificateDao
    ): CovidCertificateRepositoryFactory = CovidCertificateRepositoryFactory(
        CovidCertificateRepositoryDataBaseImpl(certificateDao),
        CovidCertificateRepositoryMockImpl(context, certificateDao)
    )

    @Provides
    @Singleton
    fun provideNotificationsSubscriptionRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): NotificationsSubscriptionRepositoryFactory =
        NotificationsSubscriptionRepositoryFactory(
            NotificationsSubscriptionRepositoryNetworkImpl(msspaApi),
            NotificationsSubscriptionRepositoryMockImpl(context)

        )

    @Provides
    @Singleton
    fun provideAppointmentRepositoryFactory(
        msspaApi: MSSPAApi
    ): AppointmentsRepositoryFactory =
        AppointmentsRepositoryFactory(
            AppointmentsRepositoryMockImpl(),
            AppointmentsRepositoryNetworkImpl(msspaApi)

        )

    @Provides
    @Singleton
    fun provideDynamicUIRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): DynamicUIRepositoryFactory =
        DynamicUIRepositoryFactory(
            DynamicUIRepositoryMockImpl(context),
            DynamicUIRepositoryNetworkImpl(msspaApi),
            DynamicUIRepositoryFileImpl(context)
        )

    @Provides
    fun provideGetUserSessionUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetUserSessionUseCase =
        GetUserSessionUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSaveUserSessionUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveSessionUseCase =
        SaveSessionUseCase(preferencesRepositoryFactory)

    @Provides
    @Singleton
    fun provideFollowUpRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi,
        session: Session
    ): MonitoringRepositoryFactory =
        MonitoringRepositoryFactory(
            MonitoringRepositoryMockImpl(context),
            MonitoringRepositoryNetworkImpl(msspaApi, session)
        )

    @Provides
    fun provideRemoveUserSessionUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): RemoveSessionUseCase =
        RemoveSessionUseCase(preferencesRepositoryFactory)

    @Provides
    @Singleton
    fun provideSessionBehaviorSubject(
        session: Session,
        getUserSessionUseCase: GetUserSessionUseCase,
        saveSessionUseCase: SaveSessionUseCase,
        removeSessionUseCase: RemoveSessionUseCase
    ): SessionBus =
        SessionBus(
            session,
            getUserSessionUseCase,
            saveSessionUseCase,
            removeSessionUseCase
        )

    @Provides
    @Singleton
    fun provideNavBackPressedBehaviorSubject(
        navBackPressed: NavBackPressed,
    ): NavBackPressedBus = NavBackPressedBus(navBackPressed)

    @Provides
    @Singleton
    fun provideLoginAdvicePressedBehaviorSubject(
        loginAdvicePressed: LoginAdvicePressed,
    ): LoginAdvicePressedBus = LoginAdvicePressedBus(loginAdvicePressed)

    @Provides
    @Singleton
    fun provideLauncherScreenBus(
        launcherScreen: LauncherScreen,
    ): LauncherScreenBus = LauncherScreenBus(launcherScreen)

    @Provides
    @Singleton
    fun provideSmsReceivedBehaviorSubject(): SmsBus = SmsBus()

    @Provides
    @Singleton
    fun provideAdviceTypeRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): AdviceTypesRepositoryFactory =
        AdviceTypesRepositoryFactory(
            AdviceTypesRepositoryMockImpl(context),
            AdviceTypesRepositoryNetworkImpl(msspaApi)

        )

    @Provides
    @Singleton
    fun provideAdvicesRepositoryFactory(
        context: Context,
        msspaApi: MSSPAApi
    ): AdvicesRepositoryFactory =
        AdvicesRepositoryFactory(
            AdvicesRepositoryMockImpl(context),
            AdvicesRepositoryNetworkImpl(msspaApi)

        )
    //endregion
}
