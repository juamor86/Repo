package es.juntadeandalucia.msspa.authentication.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.presentation.login.activities.main.AuthContract
import es.juntadeandalucia.msspa.authentication.presentation.login.activities.main.AuthPresenter

@Module
class ActivityModule {
    @Provides
    fun provideAuthPresenter(navBackPressedBus: NavBackPressedBus): AuthContract.Presenter = AuthPresenter(navBackPressedBus)
}
