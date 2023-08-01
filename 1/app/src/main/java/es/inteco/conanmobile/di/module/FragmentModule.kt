package es.inteco.conanmobile.di.module

import dagger.Module
import dagger.Provides
import es.inteco.conanmobile.di.scope.PerFragment
import es.inteco.conanmobile.domain.base.bus.LifecycleBus
import es.inteco.conanmobile.domain.usecases.*
import es.inteco.conanmobile.domain.usecases.analisys.PostAnalysisResultUseCase
import es.inteco.conanmobile.presentation.analysis.AnalysisContract
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import es.inteco.conanmobile.presentation.analysis.AnalysisPresenter
import es.inteco.conanmobile.presentation.analysis.detail.DetailContract
import es.inteco.conanmobile.presentation.analysis.detail.DetailPresenter
import es.inteco.conanmobile.presentation.analysis.permission.PermissionsContract
import es.inteco.conanmobile.presentation.analysis.permission.PermissionsPresenter
import es.inteco.conanmobile.presentation.analysis.results.ResultsContract
import es.inteco.conanmobile.presentation.analysis.results.ResultsPresenter
import es.inteco.conanmobile.presentation.analysis.results.apps.AppsContract
import es.inteco.conanmobile.presentation.analysis.results.apps.AppsPresenter
import es.inteco.conanmobile.presentation.analysis.results.settings.SettingsContract
import es.inteco.conanmobile.presentation.analysis.results.settings.SettingsPresenter
import es.inteco.conanmobile.presentation.analysis.type.AnalysisTypeContract
import es.inteco.conanmobile.presentation.analysis.type.AnalysisTypePresenter
import es.inteco.conanmobile.presentation.help.HelpContract
import es.inteco.conanmobile.presentation.help.HelpPresenter
import es.inteco.conanmobile.presentation.legal.LegalContract
import es.inteco.conanmobile.presentation.legal.LegalPresenter
import es.inteco.conanmobile.presentation.osi.OSIContract
import es.inteco.conanmobile.presentation.osi.OSIPresenter
import es.inteco.conanmobile.presentation.warnings.WarningsContract
import es.inteco.conanmobile.presentation.warnings.WarningsPresenter

@Module
class FragmentModule {

    @PerFragment
    @Provides
    fun provideDefaultAnalysisPresenter(
        getConfigurationUseCase: GetConfigurationUseCase,
        saveDefaultAnalysisUseCase: SaveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase: GetDefaultAnalysisUseCase
    ):
            AnalysisTypeContract.Presenter = AnalysisTypePresenter(
        getConfigurationUseCase,
        saveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase
    )

    @PerFragment
    @Provides
    fun provideAnalysisLaunchedPresenter(
        analysisController: AnalysisController,
        getConfigurationUseCase: GetConfigurationUseCase,
        getDefaultAnalysisUseCase: GetDefaultAnalysisUseCase,
        saveAnalysisUseCase: SaveAnalysisUseCase,
        postAnalysisResultUseCase: PostAnalysisResultUseCase,
        existLastAnalysisUseCase: ExistLastAnalysisUseCase,
        getLastAnalysisUseCase: GetLastAnalysisUseCase,
        lifecycleBus: LifecycleBus
    ): AnalysisContract.Presenter =
        AnalysisPresenter(
            analysisController,
            getConfigurationUseCase,
            getDefaultAnalysisUseCase,
            postAnalysisResultUseCase,
            saveAnalysisUseCase,
            existLastAnalysisUseCase,
            getLastAnalysisUseCase,
            lifecycleBus
        )

    @PerFragment
    @Provides
    fun provideResultsPresenter():
            ResultsContract.Presenter = ResultsPresenter()

    @PerFragment
    @Provides
    fun providesAppsPresenter(controller: AnalysisController, getConfigurationUseCase: GetConfigurationUseCase):
            AppsContract.Presenter = AppsPresenter(controller, getConfigurationUseCase)

    @PerFragment
    @Provides
    fun provideSettingsPresenter(analysisController: AnalysisController):
            SettingsContract.Presenter = SettingsPresenter(analysisController)

    @PerFragment
    @Provides
    fun provideDetailPresenter(): DetailContract.Presenter = DetailPresenter()

    @PerFragment
    @Provides
    fun providePermissionsPresenter(): PermissionsContract.Presenter = PermissionsPresenter()


    @PerFragment
    @Provides
    fun provideLegalPresenter(): LegalContract.Presenter =
        LegalPresenter()

    @PerFragment
    @Provides
    fun provideWarningsPresenter(getWarningsUseCase: GetWarningsUseCase): WarningsContract.Presenter =
        WarningsPresenter(getWarningsUseCase)

    @PerFragment
    @Provides
    fun provideOSIPresenter(getOSITipsUseCase: GetOSITipsUseCase): OSIContract.Presenter =
        OSIPresenter(getOSITipsUseCase)

    @PerFragment
    @Provides
    fun provideHelpPresenter(): HelpContract.Presenter =
        HelpPresenter()
//endregion
}