package es.juntadeandalucia.msspa.saludandalucia.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.about.AboutContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.about.AboutPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.AdvicesContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.AdvicesPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types.NewAdviceTypeContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types.NewAdviceTypePresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.AppsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.AppsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details.AppDetailsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details.AppDetailsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create.AdviceCreateContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create.AdviceCreatePresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail.AdviceDetailContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail.AdviceDetailPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.certificate.CovidCertContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.certificate.CovidCertPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.GreenPassContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.GreenPassPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.detail.CertificateDetailContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.detail.CertificateDetailPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.LoginCovidContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.LoginCovidPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.secondfactor.SecondFactorContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.secondfactor.SecondFactorPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.scanner.ScanQRContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.scanner.ScanQRPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.fragment.DynamicContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.fragment.DynamicPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.permissions.PermissionsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.permissions.PermissionsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.webview.ClicSaludWebViewContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.webview.ClicSaludWebViewPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard.DynQuestDashboardContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard.DynQuestDashboardPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.DynQuestDetailContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.DynQuestDetailPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list.DynQuestListContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list.DynQuestListPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest.DynQuestNewContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest.DynQuestNewPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.help.HelpContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.help.HelpPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.HomeContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.HomePresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.legal.LegalContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.legal.LegalPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.fragment.MainFragmentContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.fragment.MainFragmentPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.MonitoringContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.MonitoringPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard.MonitoringDashboardContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard.MonitoringDashboardPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail.MonitoringDetailContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail.MonitoringDetailPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list.MonitoringListContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list.MonitoringListPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.MeasurementsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.MeasurementsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire.NewMonitoringContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire.NewMonitoringPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.NewsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.NewsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.details.NewDetailsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.details.NewDetailsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.NotificationsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.NotificationsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.details.NotificationDetailsContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.details.NotificationDetailsPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.PreferencesContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.PreferencesPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.NotificationsStep1Contract
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.NotificationsStep1Presenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.NotificationsStep2Contract
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.NotificationsStep2Presenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.QuizContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.QuizPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.result.QuizResultContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.result.QuizResultPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.WalletContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.WalletPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.detail.WalletDetailContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.detail.WalletDetailPresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.webview.WebViewContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.webview.WebViewPresenter

@Module
class FragmentModule {

    //region - Presenters
    @PerFragment
    @Provides
    fun provideHomePresenter(
        getAppsUseCase: GetAppsUseCase,
        getFeaturedUseCase: GetFeaturedUseCase,
        getFirstAccessToValidateCertificateUseCase: GetFirstAccessUseCase,
        saveFirstAccessToValidateCertificateUseCase: SetFirstAccessUseCase,
        validateContraindicationUseCase: ValidateContraindicationUseCase,
        sessionBus: SessionBus,
        dynamicUIBus: DynamicUIBus
    ): HomeContract.Presenter = HomePresenter(
        getAppsUseCase,
        getFeaturedUseCase,
        getFirstAccessToValidateCertificateUseCase,
        saveFirstAccessToValidateCertificateUseCase,
        validateContraindicationUseCase,
        sessionBus,
        dynamicUIBus
    )

    @PerFragment
    @Provides
    fun providePreferencesPresenter(
        getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
        clearNotificationsSubscriptionUseCase: ClearNotificationsSubscriptionUseCase,
        getFirstAccessToPreferencesUseCase: GetFirstAccessUseCase,
        saveFirstAccessToPreferencesUseCase: SetFirstAccessUseCase
    ): PreferencesContract.Presenter =
        PreferencesPresenter(
            getNotificationsPhoneNumberUseCase,
            clearNotificationsSubscriptionUseCase,
            getFirstAccessToPreferencesUseCase,
            saveFirstAccessToPreferencesUseCase
        )

    @PerFragment
    @Provides
    fun provideHelpPresenter(): HelpContract.Presenter = HelpPresenter()

    @PerFragment
    @Provides
    fun provideAboutPresenter(): AboutContract.Presenter = AboutPresenter()

    @PerFragment
    @Provides
    fun provideNotificationsPresenter(
        getNotificationsUseCase: GetNotificationsUseCase,
        removeNotificationUseCase: RemoveNotificationUseCase,
        removeAllNotificationsUseCase: RemoveAllNotificationsUseCase,
        getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
        removeAllNotificationsFromStatusUseCase: RemoveAllNotificationsFromStatusUseCase,
        sendNotificationReadUseCase: SendNotificationReadUseCase,
        updateNotificationUseCase: UpdateNotificationUseCase
    ): NotificationsContract.Presenter = NotificationsPresenter(
        getNotificationsUseCase,
        removeNotificationUseCase,
        removeAllNotificationsUseCase,
        getNotificationsPhoneNumberUseCase,
        removeAllNotificationsFromStatusUseCase,
        sendNotificationReadUseCase,
        updateNotificationUseCase
    )

    @PerFragment
    @Provides
    fun provideNotificationDetailsPresenter(
        removeNotificationUseCase: RemoveNotificationUseCase
    ): NotificationDetailsContract.Presenter =
        NotificationDetailsPresenter(
            removeNotificationUseCase
        )

    @PerFragment
    @Provides
    fun provideNewsPresenter(getNewsUseCase: GetNewsUseCase): NewsContract.Presenter =
        NewsPresenter(getNewsUseCase)

    @PerFragment
    @Provides
    fun provideNewDetailsPresenter(): NewDetailsContract.Presenter = NewDetailsPresenter()

    @PerFragment
    @Provides
    fun provideAppsPresenter(getAppsUseCase: GetAppsUseCase): AppsContract.Presenter =
        AppsPresenter(getAppsUseCase)

    @PerFragment
    @Provides
    fun provideMainFragmentPresenter(): MainFragmentContract.Presenter = MainFragmentPresenter()

    @PerFragment
    @Provides
    fun provideAppDetailsPresenter(): AppDetailsContract.Presenter = AppDetailsPresenter()

    @PerFragment
    @Provides
    fun provideCoronavirusPresenter(): WebViewContract.Presenter = WebViewPresenter()

    @PerFragment
    @Provides
    fun provideLoginCovidPresenter(
        authorizeUseCase: AuthorizeUseCase,
        saveSessionUseCase: SaveQuizSessionUseCase,
        loginUseCase: LoginStep1UseCase,
        isSavedUserUseCase: IsSavedUserUseCase,
        saveUserUseCase: SaveUserUseCase,
        getSavedUsersUseCase: GetSavedUsersUseCase,
        removeUserUseCase: RemoveUserUseCase,
        getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
        getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
        setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
        showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
        showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
        getKeyValueListUseCase: GetKeyValueListUseCase
    ): LoginCovidContract.Presenter = LoginCovidPresenter(
        authorizeUseCase,
        loginUseCase,
        saveSessionUseCase,
        isSavedUserUseCase,
        saveUserUseCase,
        getSavedUsersUseCase,
        removeUserUseCase,
        getFirstSaveUserAdviceUseCase,
        getFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase,
        setFirstLoadUserAdviceUseCase,
        showSaveUserDataAlertUseCase,
        showStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase,
        getKeyValueListUseCase
    )

    @PerFragment
    @Provides
    fun provideSecondFactorPresenter(
        loginStep2UseCase: LoginStep2UseCase,
        saveSessionUseCase: SaveQuizSessionUseCase,
        saveUserUseCase: SaveUserUseCase

    ): SecondFactorContract.Presenter =
        SecondFactorPresenter(
            loginStep2UseCase,
            saveSessionUseCase,
            saveUserUseCase
        )

    @PerFragment
    @Provides
    fun provideLegalPresenter(setFirstAccessUseCase: SetFirstAccessUseCase, launcherScreenBus: LauncherScreenBus): LegalContract.Presenter =
        LegalPresenter(
            setFirstAccessUseCase, launcherScreenBus
        )

    @PerFragment
    @Provides
    fun provideQuizPresenter(
        getQuizQuestionsUseCase: GetQuizQuestionsUseCase,
        sendQuizResponsesUseCase: SendQuizResponsesUseCase,
        cancelAppointmentUseCase: CancelAppointmentUseCase,
        getQuizSessionUseCase: GetQuizSessionUseCase
    ): QuizContract.Presenter =
        QuizPresenter(getQuizQuestionsUseCase, sendQuizResponsesUseCase, cancelAppointmentUseCase, getQuizSessionUseCase)

    @PerFragment
    @Provides
    fun provideQuizResultPresenter(
        createNotificationUseCase: CreateDelayedNotificationUseCase,
        cancelAppointmentUseCase: CancelAppointmentUseCase,
        getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase
    ): QuizResultContract.Presenter =
        QuizResultPresenter(
            createNotificationUseCase,
            cancelAppointmentUseCase,
            getNotificationsPhoneNumberUseCase
        )

    @PerFragment
    @Provides
    fun provideNotificationsStep1Presenter(requestVerificationCodeUseCase: RequestVerificationCodeUseCase): NotificationsStep1Contract.Presenter =
        NotificationsStep1Presenter(requestVerificationCodeUseCase)

    @PerFragment
    @Provides
    fun provideNotificationsStep2Presenter(subscribeNotificationsUseCase: SubscribeNotificationsUseCase,
                                           smsBus: SmsBus): NotificationsStep2Contract.Presenter =
        NotificationsStep2Presenter(subscribeNotificationsUseCase,smsBus)

    @PerFragment
    @Provides
    fun provideCovidCertPresenter(
        getUserCovidCertDataUseCase: GetUserCovidCertDataUseCase,
        downloadUserCovidCertPDFUseCase: DownloadUserCovidCertPDFUseCase,
        saveCovidCertificateUseCase: SaveCovidCertificateUseCase,
        checkCovidCertificateUseCase: CheckCovidCertificateUseCase,
        getWalletIsActiveUseCase: GetWalletIsActiveUseCase,
        deleteFileUseCase: DeleteFileUseCase,
        sessionBus: SessionBus
    ): CovidCertContract.Presenter = CovidCertPresenter(
        getUserCovidCertDataUseCase,
        downloadUserCovidCertPDFUseCase,
        checkCovidCertificateUseCase,
        saveCovidCertificateUseCase,
        getWalletIsActiveUseCase,
        deleteFileUseCase,
        sessionBus
    )

    @PerFragment
    @Provides
    fun provideScanQRPresenter(
        getFirstAccessToScanCertificateUseCase: GetFirstAccessUseCase,
        saveFirstAccessToScanCertificateUseCase: SetFirstAccessUseCase,
        validateGreenPassUseCase: ValidateGreenPassUseCase,
        validateContraindicationUseCase: ValidateContraindicationUseCase
    ): ScanQRContract.Presenter =
        ScanQRPresenter(
            getFirstAccessToScanCertificateUseCase,
            saveFirstAccessToScanCertificateUseCase,
            validateGreenPassUseCase,
            validateContraindicationUseCase
        )

    @PerFragment
    @Provides
    fun providesHubPresenter(
        getFirstAccessToCertificateHubUseCase: GetFirstAccessUseCase,
        saveFirstAccessToCertificateHubUseCase: SetFirstAccessUseCase,
        sessionBus: SessionBus,
        getUserReceiptsUseCase: GetUserReceiptsUseCase
    ): GreenPassContract.Presenter =
        GreenPassPresenter(
            getFirstAccessToCertificateHubUseCase,
            saveFirstAccessToCertificateHubUseCase,
            sessionBus,
            getUserReceiptsUseCase
        )

    @PerFragment
    @Provides
    fun provideCertificateDetailPresenter(
        getUserGreenPassCertUseCase: GetUserGreenPassCertUseCase,
        downloadGreenPassPdfUseCase: DownloadGreenPassPdfUseCase,
        saveCovidUseCase: SaveCovidCertificateUseCase,
        checkCovidCertificateUseCase: CheckCovidCertificateUseCase,
        getWalletIsActiveUseCase: GetWalletIsActiveUseCase,
        deleteFileUseCase: DeleteFileUseCase,
        sessionBus: SessionBus
    ): CertificateDetailContract.Presenter =
        CertificateDetailPresenter(
            getUserGreenPassCertUseCase,
            downloadGreenPassPdfUseCase,
            saveCovidUseCase,
            checkCovidCertificateUseCase,
            getWalletIsActiveUseCase,
            deleteFileUseCase,
            sessionBus
        )

    @PerFragment
    @Provides
    fun provideDynamicPresenter(dynamicUIBus: DynamicUIBus): DynamicContract.Presenter =
        DynamicPresenter(
            dynamicUIBus
        )

    @PerFragment
    @Provides
    fun provideClicsaludPresenter(sessionBus: SessionBus): ClicSaludWebViewContract.Presenter =
        ClicSaludWebViewPresenter(sessionBus)

    @PerFragment
    @Provides
    fun provideFollowUpPresenter(
        getFollowUpUseCase: GetMonitoringUseCase
    ): MonitoringContract.Presenter =
        MonitoringPresenter(
            getFollowUpUseCase
        )

    @PerFragment
    @Provides
    fun provideMonitoringProgramPresenter(
        getNewMonitoringUseCase: GetNewMonitoringUseCase,
        getMonitoringProgramUseCase: GetMonitoringListUseCase
    ): MonitoringListContract.Presenter =
        MonitoringListPresenter(getNewMonitoringUseCase, getMonitoringProgramUseCase)

    @PerFragment
    @Provides
    fun provideMonitoringProgramDetailPresenter(): MonitoringDetailContract.Presenter =
        MonitoringDetailPresenter()

    @PerFragment
    @Provides
    fun provideNewMonitoringProgramPresenter(
        sendNewMonitoringAnswersUseCase: SendMonitoringAnswersUseCase,
        navBackPressedBus: NavBackPressedBus
    ): NewMonitoringContract.Presenter =
        NewMonitoringPresenter(sendNewMonitoringAnswersUseCase, navBackPressedBus)

    @PerFragment
    @Provides
    fun provideMeasurementsPresenter(
        getMeasureSectionUseCase: GetMeasureSectionUseCase,
        getMeasureHelperUseCase: GetMeasureHelperUseCase
    ): MeasurementsContract.Presenter =
        MeasurementsPresenter(getMeasureSectionUseCase, getMeasureHelperUseCase)


    @Provides
    fun provideMonitoringOnBoardingPresenter(
        getFirstAccessToMonitoringProgramUseCase: GetFirstAccessUseCase,
        saveFirstAccessToMonitoringProgramUseCase: SetFirstAccessUseCase,
        sessionBus: SessionBus
    ): MonitoringDashboardContract.Presenter =
        MonitoringDashboardPresenter(
            getFirstAccessToMonitoringProgramUseCase,
            saveFirstAccessToMonitoringProgramUseCase,
            sessionBus
        )

    @PerFragment
    @Provides
    fun provideWalletPresenter(
        getSharedDynamicIconsUseCase: GetSharedDynamicIconsUseCase,
        getCovidCertificatesUseCase: GetCovidCertificatesUseCase,
        setFirstAccessWalletUseCase: SetFirstAccessUseCase,
        getFirstAccessWalletUseCase: GetFirstAccessUseCase
    ): WalletContract.Presenter =
        WalletPresenter(
            getSharedDynamicIconsUseCase,
            getCovidCertificatesUseCase,
            setFirstAccessWalletUseCase,
            getFirstAccessWalletUseCase
        )

    @PerFragment
    @Provides
    fun provideWalletDetailPresenter(
        getSharedDynamicIconsUseCase: GetSharedDynamicIconsUseCase
    ): WalletDetailContract.Presenter =
        WalletDetailPresenter(getSharedDynamicIconsUseCase)

    @PerFragment
    @Provides
    fun provideAdviceListPresenter(
        getAdvicesUseCase: GetAdvicesUseCase,
        getUserSessionUseCase: GetUserSessionUseCase,
        getAdvicesReceivedUseCase: GetAdvicesReceivedUseCase,
        sessionBus: SessionBus,
        getFirstAccessToPreferencesUseCase: GetFirstAccessUseCase,
        saveFirstAccessToPreferencesUseCase: SetFirstAccessUseCase,
        getAdviceCatalogTypeNetUseCase: GetAdviceCatalogTypeNetUseCase,
        getAdviceCatalogTypePreferencesUseCase: GetAdviceCatalogTypePrefUseCase,
        saveAdviceTypeCatalogPreferencesUseCase: SaveAdviceTypeCatalogUseCase,
        isAdviceCatalogTypeUserUseCase: IsAdviceCatalogTypeUserUseCase,
        removeAdviceCatalogTypeUseCase: RemoveAdviceCatalogTypeUseCase,
        getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
        loginAdvicePressedBus: LoginAdvicePressedBus
    ): AdvicesContract.Presenter =
        AdvicesPresenter(
            getAdvicesUseCase,
            getUserSessionUseCase,
            getAdvicesReceivedUseCase,
            sessionBus,
            getFirstAccessToPreferencesUseCase,
            saveFirstAccessToPreferencesUseCase,
            getAdviceCatalogTypeNetUseCase,
            getAdviceCatalogTypePreferencesUseCase,
            saveAdviceTypeCatalogPreferencesUseCase,
            isAdviceCatalogTypeUserUseCase,
            removeAdviceCatalogTypeUseCase,
            getNotificationsPhoneNumberUseCase,
            loginAdvicePressedBus
        )

    @Provides
    fun provideNewAdviceTypePresenter(getAdviceTypesUseCase: GetAdviceTypesUseCase, sessionBus: SessionBus): NewAdviceTypeContract.Presenter =
        NewAdviceTypePresenter(getAdviceTypesUseCase, sessionBus)

    @Provides
    fun provideAdviceDetailPresenter(
        saveAdviceUseCase: SaveAdviceUseCase,
        removeAdviceUseCase: RemoveAdviceUseCase,
        getAdviceCatalogTypePreferencesUseCase: GetAdviceCatalogTypePrefUseCase,
        createAdviceChildrenUseCase: CreateAdviceChildren,
        sessionBus: SessionBus,
        navBackPressedBus: NavBackPressedBus,
    ): AdviceDetailContract.Presenter =
        AdviceDetailPresenter(
            saveAdviceUseCase,
            removeAdviceUseCase,
            createAdviceChildrenUseCase,
            getAdviceCatalogTypePreferencesUseCase,
            sessionBus,
            navBackPressedBus
        )

    @Provides
    fun provideAdviceCreatePresenter(
        createAdviceUseCase: CreateAdviceUseCase,
        sessionBus: SessionBus,
        getAdviceCatalogTypePreferencesUseCase: GetAdviceCatalogTypePrefUseCase
        ): AdviceCreateContract.Presenter =
        AdviceCreatePresenter(
            createAdviceUseCase, sessionBus, getAdviceCatalogTypePreferencesUseCase
        )


    @PerFragment
    @Provides
    fun providePermissionsPresenter(setFirstAccessUseCase: SetFirstAccessUseCase, getFirstAccessUseCase: GetFirstAccessUseCase, launcherScreenBus: LauncherScreenBus): PermissionsContract.Presenter =
        PermissionsPresenter(
            setFirstAccessUseCase, getFirstAccessUseCase, launcherScreenBus
        )

    @PerFragment
    @Provides
    fun provideDynQuestDashboardPresenter(
        getFirstAccess: GetFirstAccessUseCase,
        setFirstAccess: SetFirstAccessUseCase
    ): DynQuestDashboardContract.Presenter =
        DynQuestDashboardPresenter(getFirstAccess, setFirstAccess)

    @PerFragment
    @Provides
    fun provideDynQuestListPresenter(
        getNewDynQuestUseCase: GetNewDynQuestUseCase,
        getDynQuestListUseCase: GetDynQuestListUseCase,
        sessionBus: SessionBus
    ): DynQuestListContract.Presenter =
        DynQuestListPresenter(getNewDynQuestUseCase, getDynQuestListUseCase, sessionBus)

    @PerFragment
    @Provides
    fun provideDynQuestDetailPresenter(
    ): DynQuestDetailContract.Presenter =
        DynQuestDetailPresenter()


    @PerFragment
    @Provides
    fun provideDynQuestNewPresenter(
        getNewDynQuestUseCase: GetNewDynQuestUseCase,
        sendDynQuestAnswersUseCase: SendDynQuestAnswersUseCase,
        sessionBus: SessionBus,
        backPressedBus: NavBackPressedBus
    ): DynQuestNewContract.Presenter =
        DynQuestNewPresenter(sendDynQuestAnswersUseCase, getNewDynQuestUseCase, sessionBus,backPressedBus)

    //endregion


}
