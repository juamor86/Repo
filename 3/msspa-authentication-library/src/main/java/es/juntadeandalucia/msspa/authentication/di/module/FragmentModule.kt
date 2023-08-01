package es.juntadeandalucia.msspa.authentication.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.authentication.di.scope.PerFragment
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.domain.base.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.*
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login.LoginDnieContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login.LoginDniePresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.basic.LoginBasicContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.basic.LoginBasicPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.nonuhsa.LoginNoNuhsaContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.nonuhsa.LoginNoNuhsaPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.reinforced.LoginReinforcedContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.reinforced.LoginReinforcedPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.phonevalidation.PhoneValidationContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.phonevalidation.PhoneValidationPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.LoginQRContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.LoginQRPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.pin.PinContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.pin.PinPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.scanner.ScanQRContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.scanner.ScanQRPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification.QRVerificationContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification.QRVerificationPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.secondfactor.SecondFactorContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.secondfactor.SecondFactorPresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.web.AuthWebViewContract
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.web.AuthWebViewPresenter

@Module
class FragmentModule {
    //region - Presenters
    @PerFragment
    @Provides
    fun provideFirstFactorNoNuhsaPresenter(
        loginNoNuhsaUseCase: LoginPersonalDataNoNuhsaUseCase,
        loginPersonalDataBasicUseCase: LoginPersonalDataBasicUseCase,
        authorizeUseCase: AuthorizeUseCase,
        saveUserUseCase: SaveUserUseCase,
        getSavedUsersUseCase: GetSavedUsersUseCase,
        removeUserUseCase: RemoveUserUseCase,
        setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
        isSavedUserUseCase: IsSavedUserUseCase,
        getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
        getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
        showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
        showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase
    ): LoginNoNuhsaContract.Presenter =
        LoginNoNuhsaPresenter(
            loginNoNuhsaUseCase,
            loginPersonalDataBasicUseCase,
            authorizeUseCase,
            saveUserUseCase,
            getSavedUsersUseCase,
            removeUserUseCase,
            setFirstLoadUserAdviceUseCase,
            setFirstSaveUserAdviceUseCase,
            isSavedUserUseCase,
            getFirstLoadUserAdviceUseCase,
            getFirstSaveUserAdviceUseCase,
            showStoredUsersDataAlertUseCase,
            showSaveUserDataAlertUseCase,
            hideStoredUsersDataAlertUseCase,
            hideSaveUserDataAlertUseCase
        )

    @PerFragment
    @Provides
    fun provideFirstFactorBasicPresenter(
        loginBasicUseCase: LoginPersonalDataBasicUseCase,
        loginPersonalDataUseCase: LoginPersonalDataBasicUseCase,
        authorizeUseCase: AuthorizeUseCase,
        saveUserUseCase: SaveUserUseCase,
        getSavedUsersUseCase: GetSavedUsersUseCase,
        removeUserUseCase: RemoveUserUseCase,
        setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
        isSavedUserUseCase: IsSavedUserUseCase,
        getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
        getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
        showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
        showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase
    ): LoginBasicContract.Presenter =
        LoginBasicPresenter(
            loginBasicUseCase,
            loginPersonalDataUseCase,
            authorizeUseCase,
            saveUserUseCase,
            getSavedUsersUseCase,
            removeUserUseCase,
            setFirstLoadUserAdviceUseCase,
            setFirstSaveUserAdviceUseCase,
            isSavedUserUseCase,
            getFirstLoadUserAdviceUseCase,
            getFirstSaveUserAdviceUseCase,
            showStoredUsersDataAlertUseCase,
            showSaveUserDataAlertUseCase,
            hideStoredUsersDataAlertUseCase,
            hideSaveUserDataAlertUseCase
        )

    @PerFragment
    @Provides
    fun provideFirstFactorReinforzementPresenter(
        loginReinforcedUseCase: LoginPersonalDataReinforcedUseCase,
        loginPersonalDataUseCase: LoginPersonalDataBasicUseCase,
        authorizeUseCase: AuthorizeUseCase,
        saveUserUseCase: SaveUserUseCase,
        getSavedUsersUseCase: GetSavedUsersUseCase,
        removeUserUseCase: RemoveUserUseCase,
        setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
        isSavedUserUseCase: IsSavedUserUseCase,
        getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
        getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
        showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
        showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase
    ): LoginReinforcedContract.Presenter =
        LoginReinforcedPresenter(
            loginReinforcedUseCase,
            loginPersonalDataUseCase,
            authorizeUseCase,
            saveUserUseCase,
            getSavedUsersUseCase,
            removeUserUseCase,
            setFirstLoadUserAdviceUseCase,
            setFirstSaveUserAdviceUseCase,
            isSavedUserUseCase,
            getFirstLoadUserAdviceUseCase,
            getFirstSaveUserAdviceUseCase,
            showStoredUsersDataAlertUseCase,
            showSaveUserDataAlertUseCase,
            hideStoredUsersDataAlertUseCase,
            hideSaveUserDataAlertUseCase
        )

    @PerFragment
    @Provides
    fun provideSecondFactorPresenter(
        loginReinforcedUseCase: LoginPersonalDataReinforcedUseCase,
        loginSecondFactorUseCase: LoginPersonalDataSMSUseCase,
        authorizeUseCase: AuthorizeUseCase,
        saveUserUseCase: SaveUserUseCase
    ): SecondFactorContract.Presenter =
        SecondFactorPresenter(
            loginReinforcedUseCase,
            loginSecondFactorUseCase,
            authorizeUseCase,
            saveUserUseCase
        )

    @PerFragment
    @Provides
    fun providePhoneValidationPresenter(
        authorizeUseCase: AuthorizeUseCase,
        loginValidatePhoneUseCase: LoginValidatePhoneUseCase,
        saveUserUseCase: SaveUserUseCase
    ): PhoneValidationContract.Presenter =
        PhoneValidationPresenter(
            authorizeUseCase,
            loginValidatePhoneUseCase,
            saveUserUseCase
        )

    @PerFragment
    @Provides
    fun provideAuthWebViewPresenter(
        navBackPressedBus: NavBackPressedBus,
        setLoggingQRAttemptsUseCase: SetLoggingQRAttemptsUseCase
    ): AuthWebViewContract.Presenter =
        AuthWebViewPresenter(navBackPressedBus, setLoggingQRAttemptsUseCase)

    @PerFragment
    @Provides
    fun provideLoginQRPresenter(
        authorizeUseCase: AuthorizeUseCase,
        loginQRUseCase: LoginQRUseCase,
        getLoggingQRAttemptsUseCase: GetLoggingQRAttemptsUseCase,
        setLoggingQRAttemptsUseCase: SetLoggingQRAttemptsUseCase
    ): LoginQRContract.Presenter = LoginQRPresenter(loginQRUseCase, authorizeUseCase, getLoggingQRAttemptsUseCase, setLoggingQRAttemptsUseCase)

    @PerFragment
    @Provides
    fun providePinPresenter(
        savePinUseCase: SavePinUseCase,
        navBackPressedBus: NavBackPressedBus
    ): PinContract.Presenter = PinPresenter(savePinUseCase, navBackPressedBus)

    @PerFragment
    @Provides
    fun provideQrVerificationPresenter(
        checkPinUseCase: CheckPinUseCase,
        refreshTokenUseCase: RefreshTokenUseCase,
        removePinUseCase: RemovePinUseCase,
        validateTokenUseCase: ValidateTokenUseCase,
        invalidateTokenUseCase: InvalidateTokenUseCase
    ): QRVerificationContract.Presenter =
        QRVerificationPresenter(
            checkPinUseCase,
            refreshTokenUseCase,
            removePinUseCase,
            validateTokenUseCase,
            invalidateTokenUseCase
        )

    @PerFragment
    @Provides
    fun provideAddCanPresenter( loginDniUseCase: LoginDniUseCase, authorizeUseCase: AuthorizeUseCase
    ): LoginDnieContract.Presenter =
        LoginDniePresenter( authorizeUseCase, loginDniUseCase)


    @PerFragment
    @Provides
    fun provideScanQRPresenter(
    ): ScanQRContract.Presenter =
        ScanQRPresenter()


}