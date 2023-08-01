package es.juntadeandalucia.msspa.authentication.presentation.login.activities.main

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract

class AuthContract {

    interface View : BaseContract.View {
        fun configureView()
        fun navigateToBack()
        fun navigateToDest(dest: Int)
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(
            config: MsspaAuthenticationConfig,
            scope: MsspaAuthenticationManager.Scope,
            value: Int?
        )
        fun checkBackPressed()
        fun onNavPressedBack(navType: NavBackPressed.NavBackType)
    }
}
