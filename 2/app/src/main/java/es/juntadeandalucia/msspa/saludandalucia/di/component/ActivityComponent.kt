package es.juntadeandalucia.msspa.saludandalucia.di.component

import dagger.Component
import es.juntadeandalucia.msspa.saludandalucia.di.module.*
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerActivity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicFeedbackController
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigationController
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.HmsTokenProvider
import es.juntadeandalucia.msspa.saludandalucia.presentation.splash.SplashActivity

@PerActivity
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        ActivityModule::class,
        ContentModule::class,
        UserModule::class,
        CertificateModule::class,
        NotificationsModule::class]
)
interface ActivityComponent {

    val dynamicNavigationController: DynamicNavigationController

    val dynamicFeedbackController:  DynamicFeedbackController

    fun inject(activity: MainActivity)

    fun inject(activity: SplashActivity)

    val hmsTokenProvider: HmsTokenProvider
}
