package es.juntadeandalucia.msspa.authentication.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.AuthorizeRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.di.scope.PerActivity
import es.juntadeandalucia.msspa.authentication.di.scope.PerFragment
import es.juntadeandalucia.msspa.authentication.domain.base.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.*

@Module
class UserModule {
    //region - Use cases
    @Provides
    fun providesLoginPersonalDataNoNuhsaUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): LoginPersonalDataNoNuhsaUseCase = LoginPersonalDataNoNuhsaUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun providesLoginPersonalDataBasicUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): LoginPersonalDataBasicUseCase = LoginPersonalDataBasicUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun providesLoginPersonalDataReinforcedUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): LoginPersonalDataReinforcedUseCase = LoginPersonalDataReinforcedUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun providesLoginValidatePhoneUseCase(
        loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): LoginValidatePhoneUseCase = LoginValidatePhoneUseCase(
        loginPersonalDataRepositoryFactory
    )

    @Provides
    fun provideAuthorizeUseCase(
        msspaAuthenticationConfig: MsspaAuthenticationConfig,
        authorizeRepositoryFactory: AuthorizeRepositoryFactory
    ): AuthorizeUseCase = AuthorizeUseCase(
        msspaAuthenticationConfig,
        authorizeRepositoryFactory
    )

    @PerFragment
    @Provides
    fun provideLoginPersonalDataStep2UseCase(
        loginRepositoryFactory: LoginPersonalDataRepositoryFactory
    ): LoginPersonalDataSMSUseCase = LoginPersonalDataSMSUseCase(
        loginRepositoryFactory
    )

    @PerFragment
    @Provides
    fun provideSaveUserUseCase(
        preferencesRepositoryFactory: PreferencesRepositoryFactory
    ): SaveUserUseCase = SaveUserUseCase(
        preferencesRepositoryFactory
    )

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

    @PerFragment
    @Provides
    fun provideRemoveAllUsersUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): RemoveAllUsersUseCase =
        RemoveAllUsersUseCase(
            preferencesRepositoryFactory
        )

    @PerFragment
    @Provides
    fun provideIsSavedUserUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): IsSavedUserUseCase =
        IsSavedUserUseCase(
            preferencesRepositoryFactory
        )

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

    @PerFragment
    @Provides
    fun provideCheckPinUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory) : CheckPinUseCase = CheckPinUseCase(preferencesRepositoryFactory )

    @PerFragment
    @Provides
    fun provideRemovePinUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory) : RemovePinUseCase = RemovePinUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetLoggingQRAttemptsUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetLoggingQRAttemptsUseCase =
        GetLoggingQRAttemptsUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSetLoggingQRAttemptsUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): SetLoggingQRAttemptsUseCase =
        SetLoggingQRAttemptsUseCase(preferencesRepositoryFactory)

    //endregion
}
