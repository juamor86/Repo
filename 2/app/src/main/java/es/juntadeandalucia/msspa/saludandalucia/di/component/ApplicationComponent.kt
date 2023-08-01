package es.juntadeandalucia.msspa.saludandalucia.di.component

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPALoginApi
import es.juntadeandalucia.msspa.saludandalucia.data.factory.*
import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.KeyValueRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.AppDataBase
import es.juntadeandalucia.msspa.saludandalucia.device.notification.NotificationServiceImpl
import es.juntadeandalucia.msspa.saludandalucia.di.module.ApplicationModule
import es.juntadeandalucia.msspa.saludandalucia.di.module.NetModule
import es.juntadeandalucia.msspa.saludandalucia.di.module.NotificationsModule
import es.juntadeandalucia.msspa.saludandalucia.di.module.PersistenceModule
import es.juntadeandalucia.msspa.saludandalucia.di.module.ServiceModule
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.*
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LauncherScreen
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.DynamicUIBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.NavBackPressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.GmsMessagingService
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.HmsMessagingService
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, NetModule::class, PersistenceModule::class, ServiceModule::class,
        NotificationsModule::class]
)
interface ApplicationComponent {

    //region - Expose sub-graphs dependencies
    val preferences: SharedPreferences

    val context: Context

    val mSSPAApiService: MSSPAApi

    val mSSPALoginApiService: MSSPALoginApi

    val quizSession: QuizSession?

    val session: Session

    val navBackPressed: NavBackPressed

    val launcherScreen: LauncherScreen

    val cryptographyManager: CrytographyManager?

    val notificationService: NotificationService

    val userRepositoryFactory: UserRepositoryFactory

    val announcementsRepositoryFactory: AnnouncementsRepositoryFactory

    val feedbackRepositoryFactory: FeedbackRepositoryFactory

    val newsRepositoryFactory: NewsRepositoryFactory

    val appsRepositoryFactory: AppsRepositoryFactory

    val featuredRepositoryFactory: FeaturedRepositoryFactory

    val preferencesRepositoryFactory: PreferencesRepositoryFactory

    val notificationsRepositoryFactory: NotificationsRepositoryFactory

    val covidCertificateRepository: CovidCertificateRepositoryFactory

    val appointmentsRepositoryFactory: AppointmentsRepositoryFactory

    val notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory

    val authorizeRepositoryFactory: AuthorizeRepositoryFactory

    val loginRepositoryFactory: LoginRepositoryFactory

    val quizRepositoryFactory: QuizRepositoryFactory

    val dynQuestRepositoryFactory: DynamicQuestionnairesRepositoryFactory

    val dynamicUIRepositoryFactory: DynamicUIRepositoryFactory

    val appDataBase: AppDataBase

    val adviceTypesRepositoryFactory: AdviceTypesRepositoryFactory

    val advicesRepositoryFactory: AdvicesRepositoryFactory

    val keyValueRepositoryFactory: KeyValueRepositoryFactory

    val monitoringRepositoryFactory: MonitoringRepositoryFactory

    val sessionBus: SessionBus

    val dynamicUIBus: DynamicUIBus

    val smsBus: SmsBus

    val navBackPressedBus: NavBackPressedBus

    val loginAdvicePressedBus: LoginAdvicePressedBus

    val launcherScreenBus: LauncherScreenBus

    //endregion

    fun inject(application: App)

    fun inject(myFirebaseMessagingService: GmsMessagingService)

    fun inject(myHmsMessagingService: HmsMessagingService)

    fun inject(delayedNotificationWorker: NotificationServiceImpl.DelayedNotificationWorker)
}
