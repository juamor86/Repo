package es.juntadeandalucia.msspa.saludandalucia.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.factory.NotificationsSubscriptionRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*

@Module
class NotificationsModule {

    @Provides
    fun provideSaveFirebaseTokenUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveHmsGmsTokenUseCase =
        SaveHmsGmsTokenUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideCheckNotificationsEnabledUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetNotificationsPhoneNumberUseCase =
        GetNotificationsPhoneNumberUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetHmsGmsTokenUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetHmsGmsTokenUseCase =
        GetHmsGmsTokenUseCase(preferencesRepositoryFactory)

    @Provides
    @PerFragment
    fun provideRequestVerificationCodeUseCase(notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory): RequestVerificationCodeUseCase =
        RequestVerificationCodeUseCase(notificationsSubscriptionRepositoryFactory)

    @Provides
    @PerFragment
    fun provideSubscribeNotificationsUseCase(
        notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory,
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ): SubscribeNotificationsUseCase =
        SubscribeNotificationsUseCase(notificationsSubscriptionRepositoryFactory, preferencesRepositoryFactory)

    @Provides
    @PerFragment
    fun provideClearNotificationsSubscriptionUseCase(
        notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory,
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ): ClearNotificationsSubscriptionUseCase =
        ClearNotificationsSubscriptionUseCase(notificationsSubscriptionRepositoryFactory, preferencesRepositoryFactory)

    @Provides
    fun provideUpdateNotificationsSubscriptionUseCase(
        notificationsSubscriptionRepositoryFactory: NotificationsSubscriptionRepositoryFactory,
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ): UpdateNotificationsSubscriptionUseCase =
        UpdateNotificationsSubscriptionUseCase(notificationsSubscriptionRepositoryFactory, preferencesRepositoryFactory)
}
