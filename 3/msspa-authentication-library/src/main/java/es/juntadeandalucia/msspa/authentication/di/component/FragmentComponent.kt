package es.juntadeandalucia.msspa.authentication.di.component

import dagger.Component
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.di.module.UserModule
import es.juntadeandalucia.msspa.authentication.di.scope.PerFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login.LoginDnieFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.basic.LoginBasicFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.nonuhsa.LoginNoNuhsaFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.reinforced.LoginReinforcedFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.phonevalidation.PhoneValidationFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.LoginQRFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.pin.PinFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.scanner.ScanQRFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr.verification.QRVerificationFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.secondfactor.SecondFactorFragment
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.web.AuthWebViewFragment

@PerFragment
@Component(
    dependencies = [AuthLibraryComponent::class],
    modules = [
        FragmentModule::class,
        UserModule::class,
        NetModule::class
    ]
)

interface FragmentComponent {
    fun inject(fragment: AuthWebViewFragment)
    fun inject(fragment: LoginNoNuhsaFragment)
    fun inject(fragment: LoginBasicFragment)
    fun inject(fragment: LoginReinforcedFragment)
    fun inject(fragment: SecondFactorFragment)
    fun inject(fragment: PhoneValidationFragment)
    fun inject(fragment: LoginQRFragment)
    fun inject(fragment: PinFragment)
    fun inject(fragment: QRVerificationFragment)
    fun inject(fragment: LoginDnieFragment)
    fun inject(fragment: ScanQRFragment)
}