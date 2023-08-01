package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.factory.*
import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.KeyValueRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*

@Module
class UserModule {

    //region - Use cases

    @Provides
    fun provideGetFirstAccessUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetFirstAccessUseCase =
        GetFirstAccessUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideIsSavedUserUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): IsSavedUserUseCase =
        IsSavedUserUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideSaveUserUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveUserUseCase =
        SaveUserUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideGetUserSessionUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetUserSessionUseCase =
        GetUserSessionUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetSavedUsersUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetSavedUsersUseCase =
        GetSavedUsersUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideRemoveUserUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): RemoveUserUseCase =
        RemoveUserUseCase(
            preferencesRepositoryFactory
        )

    @Provides
    fun provideSetFirstAccessUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetFirstAccessUseCase =
        SetFirstAccessUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideAuthorizeUseCase(
        authorizeRepositoryFactory: AuthorizeRepositoryFactory
    ): AuthorizeUseCase = AuthorizeUseCase(
        authorizeRepositoryFactory
    )

    @PerFragment
    @Provides
    fun provideLoginUseCase(
        loginRepositoryFactory: LoginRepositoryFactory
    ): LoginStep1UseCase = LoginStep1UseCase(
        loginRepositoryFactory
    )

    @PerFragment
    @Provides
    fun provideLoginStep2UseCase(
        loginRepositoryFactory: LoginRepositoryFactory
    ): LoginStep2UseCase = LoginStep2UseCase(
        loginRepositoryFactory
    )

    @PerFragment
    @Provides
    fun provideSaveQuizSessionUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveQuizSessionUseCase =
        SaveQuizSessionUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetQuizSessionUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetQuizSessionUseCase =
        GetQuizSessionUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetKeyValueListUseCase(keyValueRepositoryFactory: KeyValueRepositoryFactory): GetKeyValueListUseCase =
        GetKeyValueListUseCase(keyValueRepositoryFactory)

    @Provides
    fun provideCancelAppointmentUseCase(appointmentsRepositoryFactory: AppointmentsRepositoryFactory): CancelAppointmentUseCase =
        CancelAppointmentUseCase(appointmentsRepositoryFactory)

    @Provides
    fun provideGetMeasureSectionUseCase(userRepositoryFactory: UserRepositoryFactory): GetMeasureSectionUseCase =
        GetMeasureSectionUseCase(userRepositoryFactory)

    @Provides
    fun provideGetFollowUpUseCase(followUpRepositoryFactory: MonitoringRepositoryFactory): GetMonitoringUseCase =
        GetMonitoringUseCase(followUpRepositoryFactory)

    @Provides
    fun provideGetUserGreenPassUseCase(
        context: Context,
        session: Session,
        userRepositoryFactory: UserRepositoryFactory
    ): GetUserGreenPassCertUseCase =
        GetUserGreenPassCertUseCase(context, session, userRepositoryFactory)

    @Provides
    fun provideDownloadUserGreenPassCertPdfUseCase(userRepositoryFactory: UserRepositoryFactory): DownloadGreenPassPdfUseCase =
        DownloadGreenPassPdfUseCase(userRepositoryFactory)

    @Provides
    fun provideGetMonitoringProgramUseCase(followUpRepositoryFactory: MonitoringRepositoryFactory): GetMonitoringListUseCase =
        GetMonitoringListUseCase(followUpRepositoryFactory)

    @Provides
    fun provideNewGetMonitoringProgramUseCase(followUpRepositoryFactory: MonitoringRepositoryFactory): GetNewMonitoringUseCase =
        GetNewMonitoringUseCase(followUpRepositoryFactory)

    @Provides
    fun provideGetMeasureHelperUseCase(userRepositoryFactory: UserRepositoryFactory): GetMeasureHelperUseCase =
        GetMeasureHelperUseCase(userRepositoryFactory)

    @Provides
    fun provideGetUserReceiptsUseCase(userRepositoryFactory: UserRepositoryFactory): GetUserReceiptsUseCase =
        GetUserReceiptsUseCase(userRepositoryFactory)

    @Provides
    fun provideDeleteFileTemUseCase(): DeleteFileUseCase =
        DeleteFileUseCase()

    @Provides
    fun provideSaveLastUpdateDynReleasesUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SaveLastUpdateDynReleasesUseCase =
        SaveLastUpdateDynReleasesUseCase(
            preferencesRepositoryFactory
        )

    @Provides
    fun provideGetLastUpdateDynReleasesUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetLastUpdateDynReleasesUseCase =
        GetLastUpdateDynReleasesUseCase(
            preferencesRepositoryFactory
        )

    //endregion
}
