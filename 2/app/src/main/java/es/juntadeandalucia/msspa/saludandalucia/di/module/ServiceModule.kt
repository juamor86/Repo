package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.NotificationDao
import es.juntadeandalucia.msspa.saludandalucia.device.notification.NotificationServiceImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendNotificationReceivedUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.UpdateNotificationUseCase
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideNotificationService(
        context: Context,
        notificationDao: NotificationDao,
        sendNotificationReceivedUseCase: SendNotificationReceivedUseCase,
        updateNotificationUseCase: UpdateNotificationUseCase
    ): NotificationService =
        NotificationServiceImpl(context, notificationDao, sendNotificationReceivedUseCase, updateNotificationUseCase)

    @Provides
    @Singleton
    fun providesFirebaseMessagingService(fcmService: FirebaseMessagingService) = fcmService
}
