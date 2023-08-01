package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.factory.*
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerActivity
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.*

@Module
class ContentModule {

    //region - Use cases
    @PerFragment
    @Provides
    fun provideGetAnnouncementsUseCase(
        announcementsRepositoryFactory: AnnouncementsRepositoryFactory
    ): GetAnnouncementsUseCase = GetAnnouncementsUseCase(announcementsRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetAppsUseCase(appsRepositoryFactory: AppsRepositoryFactory): GetAppsUseCase =
        GetAppsUseCase(appsRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetNewsUseCase(newsRepositoryFactory: NewsRepositoryFactory): GetNewsUseCase =
        GetNewsUseCase(newsRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetFeaturedUseCase(featuredRepositoryFactory: FeaturedRepositoryFactory): GetFeaturedUseCase =
        GetFeaturedUseCase(featuredRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetNotificationsUseCase(notificationsRepositoryFactory: NotificationsRepositoryFactory): GetNotificationsUseCase =
        GetNotificationsUseCase(notificationsRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetCovidCertificatesUseCase(covidCertificateRepositoryFactory: CovidCertificateRepositoryFactory) :GetCovidCertificatesUseCase =
        GetCovidCertificatesUseCase(covidCertificateRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSendNotificationReadedUseCase(notificationsRepositoryFactory: NotificationsRepositoryFactory): SendNotificationReadUseCase =
        SendNotificationReadUseCase(notificationsRepositoryFactory)
    @PerFragment
    @Provides
    fun provideRemoveNotificationUseCase(notificationsRepositoryFactory: NotificationsRepositoryFactory): RemoveNotificationUseCase =
        RemoveNotificationUseCase(notificationsRepositoryFactory)

    @PerFragment
    @Provides
    fun provideRemoveAllNotificationsUseCase(notificationsRepositoryFactory: NotificationsRepositoryFactory): RemoveAllNotificationsUseCase =
        RemoveAllNotificationsUseCase(notificationsRepositoryFactory)

    @PerFragment
    @Provides
    fun provideUpdateNotificationUseCase(notificationsRepositoryFactory: NotificationsRepositoryFactory): UpdateNotificationUseCase =
        UpdateNotificationUseCase(notificationsRepositoryFactory)

    @PerActivity
    @Provides
    fun provideGetNotificationsNotReadedCountUseCase(notificationsRepositoryFactory: NotificationsRepositoryFactory): GetNotificationsNotReadedCountUseCase =
        GetNotificationsNotReadedCountUseCase(notificationsRepositoryFactory)

    @Provides
    fun provideSaveSharedDataUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ) : SaveSharedDynamicIconsUseCase =
        SaveSharedDynamicIconsUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideGetServiceEventsUseCase(
        feedbackRepositoryFactory: FeedbackRepositoryFactory
    ): GetServiceEventsUseCase =
        GetServiceEventsUseCase(feedbackRepositoryFactory)

    @Provides
    fun provideGetSavedLikeItUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ): GetSavedLikeItUseCase =
        GetSavedLikeItUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSaveLikeItUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ): SaveLikeItUseCase =
        SaveLikeItUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideExecuteJavascriptUseCase(
        context: Context
    ): ExecuteJavascriptUseCase =
        ExecuteJavascriptUseCase(context)

    @Provides
    fun provideGetSharedDataUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ) : GetSharedDynamicIconsUseCase =
        GetSharedDynamicIconsUseCase(preferencesRepositoryFactory)
    @PerFragment
    @Provides
    fun provideCreateAdviceUseCase(advicesRepositoryFactory: AdvicesRepositoryFactory): CreateAdviceUseCase =
        CreateAdviceUseCase(advicesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetAdvicesUseCase(context: Context, advicesRepositoryFactory: AdvicesRepositoryFactory): GetAdvicesUseCase =
        GetAdvicesUseCase(context,advicesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideCreateAdviceChildrenUseCase(advicesRepositoryFactory: AdvicesRepositoryFactory): CreateAdviceChildren =
        CreateAdviceChildren(advicesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetAdvicesReceivedUseCase(context: Context,advicesRepositoryFactory: AdvicesRepositoryFactory): GetAdvicesReceivedUseCase =
        GetAdvicesReceivedUseCase(context,advicesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetAdviceTypesUseCase(adviceTypesRepositoryFactory: AdviceTypesRepositoryFactory): GetAdviceTypesUseCase =
        GetAdviceTypesUseCase(adviceTypesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideRemoveAdviceUseCase(advicesRepositoryFactory: AdvicesRepositoryFactory): RemoveAdviceUseCase =
        RemoveAdviceUseCase(advicesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSaveAdviceUseCase(advicesRepositoryFactory: AdvicesRepositoryFactory): SaveAdviceUseCase =
        SaveAdviceUseCase(advicesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideIsAdviceCatalogTypeUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): IsAdviceCatalogTypeUserUseCase =
        IsAdviceCatalogTypeUserUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideSaveAdviceCatalogTypeUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveAdviceTypeCatalogUseCase =
        SaveAdviceTypeCatalogUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideGetAdviceCatalogTypePrefUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetAdviceCatalogTypePrefUseCase =
        GetAdviceCatalogTypePrefUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideRemoveAdviceCatalogTypeUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): RemoveAdviceCatalogTypeUseCase =
        RemoveAdviceCatalogTypeUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideGetAdviceCatalogTypeNetUseCase(advicesRepositoryFactory: AdviceTypesRepositoryFactory): GetAdviceCatalogTypeNetUseCase =
        GetAdviceCatalogTypeNetUseCase(
            advicesRepositoryFactory
        )
    //endregion
}
