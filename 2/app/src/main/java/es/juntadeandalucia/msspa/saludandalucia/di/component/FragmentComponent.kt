package es.juntadeandalucia.msspa.saludandalucia.di.component

import dagger.Component
import es.juntadeandalucia.msspa.saludandalucia.di.module.*
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.about.AboutFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.AdvicesFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types.NewAdviceTypeFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.AppsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details.AppDetailsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create.AdviceCreateFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail.AdviceDetailFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.certificate.CovidCertFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.GreenPassFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.detail.CertificateDetailFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.LoginCovidFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.secondfactor.SecondFactorFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.scanner.ScanQRFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.fragment.DynamicFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.permissions.PermissionsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.webview.ClicSaludWebViewFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard.DynQuestDashboardFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.DynQuestDetailFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.help.DynQuestHelpFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list.DynQuestListFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest.DynQuestNewFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.help.HelpFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.home.HomeFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.legal.LegalFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.MonitoringFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard.MonitoringDashboardFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail.MonitoringDetailFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list.MonitoringListFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.MeasurementsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire.NewMonitoringFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.NewsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.details.NewDetailsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.NotificationsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.details.NotificationDetailsFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.PreferencesFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.NotificationsStep1Fragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.NotificationsStep2Fragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.QuizFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.result.QuizResultFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.WalletFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.detail.WalletDetailFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.webview.WebViewFragment

@PerFragment
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        FragmentModule::class,
        ContentModule::class,
        UserModule::class,
        QuizModule::class,
        DynQuestModule::class,
        NotificationsModule::class,
        CertificateModule::class
    ]
)
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: PreferencesFragment)

    fun inject(fragment: HelpFragment)

    fun inject(fragment: AboutFragment)

    fun inject(fragment: NotificationsFragment)

    fun inject(fragment: NotificationDetailsFragment)

    fun inject(fragment: NewsFragment)

    fun inject(fragment: NewDetailsFragment)

    fun inject(fragment: AppsFragment)

    fun inject(fragment: AppDetailsFragment)

    fun inject(fragment: WebViewFragment)

    fun inject(fragment: LegalFragment)

    fun inject(fragment: LoginCovidFragment)

    fun inject(fragment: SecondFactorFragment)

    fun inject(fragment: QuizFragment)

    fun inject(fragment: QuizResultFragment)

    fun inject(fragment: NotificationsStep1Fragment)

    fun inject(fragment: NotificationsStep2Fragment)

    fun inject(fragment: CovidCertFragment)

    fun inject(fragment: ScanQRFragment)

    fun inject(fragment: GreenPassFragment)

    fun inject(fragment: CertificateDetailFragment)

    fun inject(fragment: DynamicFragment)

    fun inject(fragment: MonitoringFragment)

    fun inject(fragment: ClicSaludWebViewFragment)

    fun inject(fragment: MonitoringListFragment)

    fun inject(fragment: MonitoringDetailFragment)

    fun inject(fragment: NewMonitoringFragment)

    fun inject(fragment: MeasurementsFragment)

    fun inject(fragment: MonitoringDashboardFragment)

    fun inject(fragment: WalletFragment)

    fun inject(fragment: WalletDetailFragment)

    fun inject(fragment: AdvicesFragment)

    fun inject(fragment: NewAdviceTypeFragment)

    fun inject(fragment: AdviceDetailFragment)

    fun inject(fragment: AdviceCreateFragment)

    fun inject(fragment: PermissionsFragment)

    fun inject(fragment: DynQuestDashboardFragment)

    fun inject(fragment: DynQuestListFragment)

    fun inject(fragment: DynQuestDetailFragment)

    fun inject(fragment: DynQuestNewFragment)

    fun inject(fragment: DynQuestHelpFragment)
}
