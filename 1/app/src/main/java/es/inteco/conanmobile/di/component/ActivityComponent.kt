package es.inteco.conanmobile.di.component

import dagger.Component
import es.inteco.conanmobile.di.module.ActivityModule
import es.inteco.conanmobile.di.module.ContentModule
import es.inteco.conanmobile.di.scope.PerActivity
import es.inteco.conanmobile.presentation.main.MainActivity

import es.inteco.conanmobile.presentation.splash.SplashActivity

@PerActivity
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        ActivityModule::class,
        ContentModule::class
    ]
)
interface ActivityComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: SplashActivity)
}