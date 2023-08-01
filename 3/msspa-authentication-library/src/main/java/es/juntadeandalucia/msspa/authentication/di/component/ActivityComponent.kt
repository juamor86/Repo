package es.juntadeandalucia.msspa.authentication.di.component

import dagger.Component
import es.juntadeandalucia.msspa.authentication.di.module.ActivityModule
import es.juntadeandalucia.msspa.authentication.di.module.UserModule
import es.juntadeandalucia.msspa.authentication.di.scope.PerActivity
import es.juntadeandalucia.msspa.authentication.presentation.login.activities.main.AuthActivity
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification.QRVerificationFragment

@PerActivity
@Component(
    dependencies = [AuthLibraryComponent::class],
    modules = [
        ActivityModule::class,
        UserModule::class
    ]
)
interface ActivityComponent {
    fun inject(activity: AuthActivity)
}
