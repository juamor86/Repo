package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerApplicationComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.ApplicationModule
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsPhoneNumberUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveHmsGmsTokenUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.UpdateNotificationsSubscriptionUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import javax.inject.Inject
import timber.log.Timber

class GmsMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    internal lateinit var saveHmsGmsTokenUseCase: SaveHmsGmsTokenUseCase

    @Inject
    internal lateinit var getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase

    @Inject
    internal lateinit var updateNotificationsSubscriptionUseCase: UpdateNotificationsSubscriptionUseCase

    override fun onCreate() {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(application as App))
            .build()
            .inject(this)

        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        App.baseComponent.inject(this)
        super.onMessageReceived(remoteMessage)
        notificationService.handleFCMMessage(remoteMessage)
        Timber.d("Firebase Message received: ${remoteMessage.notification?.body}")
    }

    override fun onNewToken(token: String) {
        App.baseComponent.inject(this)
        super.onNewToken(token)
        Timber.d("Firebase token: $token")
        NotificationsSubscriptionHandler(
            saveHmsGmsTokenUseCase,
            getNotificationsPhoneNumberUseCase,
            updateNotificationsSubscriptionUseCase
        ).checkNotificationsEnabled(token)
    }
}
