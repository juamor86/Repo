package es.juntadeandalucia.msspa.saludandalucia.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.factory.QuizRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.CreateDelayedNotificationUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetFirstLoadUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetFirstSaveUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetQuizQuestionsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.HideSaveUserDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.HideStoredUsersDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.RemoveAllNotificationsFromStatusUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendQuizResponsesUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.SetFirstLoadUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.SetFirstSaveUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ShowSaveUserDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ShowStoredUsersDataAlertUseCase

@Module
class QuizModule {

    //region - Use cases
    @PerFragment
    @Provides
    fun provideGetQuizQuestionsUseCase(quizRepositoryFactory: QuizRepositoryFactory): GetQuizQuestionsUseCase =
        GetQuizQuestionsUseCase(quizRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSendQuizQuestionsUseCase(quizRepositoryFactory: QuizRepositoryFactory): SendQuizResponsesUseCase =
        SendQuizResponsesUseCase(quizRepositoryFactory)

    @PerFragment
    @Provides
    fun provideCreateNotificationUseCase(
        notificationService: NotificationService
    ): CreateDelayedNotificationUseCase = CreateDelayedNotificationUseCase(notificationService)

    @PerFragment
    @Provides
    fun provideRemoveAllNotificationsFromStatusUseCase(
        notificationService: NotificationService
    ): RemoveAllNotificationsFromStatusUseCase = RemoveAllNotificationsFromStatusUseCase(notificationService)

    @PerFragment
    @Provides
    fun provideGetFirstSaveUserAdviceUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetFirstSaveUserAdviceUseCase =
        GetFirstSaveUserAdviceUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetFirstLoadUserAdviceUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetFirstLoadUserAdviceUseCase =
        GetFirstLoadUserAdviceUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSetFirstSaveUserAdviceUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetFirstSaveUserAdviceUseCase =
        SetFirstSaveUserAdviceUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSetFirstLoadUserAdviceUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetFirstLoadUserAdviceUseCase =
        SetFirstLoadUserAdviceUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideShowSaveUserDataAlertUseCase(): ShowSaveUserDataAlertUseCase = ShowSaveUserDataAlertUseCase()

    @PerFragment
    @Provides
    fun provideHideSaveUserDataAlertUseCase(): HideSaveUserDataAlertUseCase = HideSaveUserDataAlertUseCase()

    @PerFragment
    @Provides
    fun provideShowStoredUsersDataAlertUseCase(): ShowStoredUsersDataAlertUseCase = ShowStoredUsersDataAlertUseCase()

    @PerFragment
    @Provides
    fun provideHideStoredUsersDataAlertUseCase(): HideStoredUsersDataAlertUseCase = HideStoredUsersDataAlertUseCase()
    //endregion
}
