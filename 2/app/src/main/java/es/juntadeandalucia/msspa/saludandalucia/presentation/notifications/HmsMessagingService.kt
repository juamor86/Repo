package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerApplicationComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.ApplicationModule
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsPhoneNumberUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveHmsGmsTokenUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.UpdateNotificationsSubscriptionUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import javax.inject.Inject
import timber.log.Timber

class HmsMessagingService : HmsMessageService() {
    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    internal lateinit var saveHmsGmsTokenUseCase: SaveHmsGmsTokenUseCase

    @Inject
    internal lateinit var getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase

    @Inject
    internal lateinit var updateNotificationsSubscriptionUseCase: UpdateNotificationsSubscriptionUseCase

    override fun onCreate() {
        Timber.d("HMS Initializated")

        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(application as App))
            .build()
            .inject(this)

        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        App.baseComponent.inject(this)
        if (message != null) {
            notificationService.handleHMSMessage(message)
            Timber.d("HMS Message received: ${message.data}")
        }
    }

    override fun onNewToken(token: String?) {
        App.baseComponent.inject(this)
        token?.let {
            Timber.d("HMS token: $token")
            NotificationsSubscriptionHandler(
                saveHmsGmsTokenUseCase,
                getNotificationsPhoneNumberUseCase,
                updateNotificationsSubscriptionUseCase
            ).checkNotificationsEnabled(token)
        }
    }
}
