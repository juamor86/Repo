package es.inteco.conanmobile.di.component

import dagger.Component
import es.inteco.conanmobile.di.module.ContentModule
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.di.scope.PerFragment
import es.inteco.conanmobile.presentation.analysis.AnalysisFragment
import es.inteco.conanmobile.presentation.analysis.detail.DetailFragment
import es.inteco.conanmobile.presentation.analysis.permission.PermissionsFragment
import es.inteco.conanmobile.presentation.analysis.results.ResultsFragment
import es.inteco.conanmobile.presentation.analysis.results.apps.AppsFragment
import es.inteco.conanmobile.presentation.analysis.results.settings.SettingsFragment
import es.inteco.conanmobile.presentation.analysis.type.AnalysisTypeFragment
import es.inteco.conanmobile.presentation.help.HelpFragment
import es.inteco.conanmobile.presentation.legal.LegalFragment
import es.inteco.conanmobile.presentation.osi.OSIFragment
import es.inteco.conanmobile.presentation.warnings.WarningsFragment

@PerFragment
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        FragmentModule::class,
        ContentModule::class
    ]
)
interface FragmentComponent {

    fun inject(fragment: AnalysisTypeFragment)

    fun inject(fragment: AnalysisFragment)

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: AppsFragment)

    fun inject(fragment: ResultsFragment)

    fun inject(fragment: DetailFragment)

    fun inject(fragment: PermissionsFragment)

    fun inject(fragment: LegalFragment)

    fun inject(fragment: WarningsFragment)

    fun inject(fragment: OSIFragment)

    fun inject(fragment: HelpFragment)
}