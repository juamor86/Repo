package es.juntadeandalucia.msspa.authentication.presentation.login.activities.main

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.domain.NavBackPressedBus
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter

class AuthPresenter(private val navBackPressedBus: NavBackPressedBus) :
    BasePresenter<AuthContract.View>(), AuthContract.Presenter {

    override fun onCreate(config: MsspaAuthenticationConfig, scope: MsspaAuthenticationManager.Scope, dest: Int?) {
        view.configureView()
        if (dest != null) {
            view.navigateToDest(dest)
        }
    }

    override fun checkBackPressed() {
        view.navigateToBack()
    }

    override fun unsubscribe() {
        navBackPressedBus.clear()
    }

    override fun onNavPressedBack(navType: NavBackPressed.NavBackType) {
        navBackPressedBus.createNavBackPressed(navType)
    }
}
