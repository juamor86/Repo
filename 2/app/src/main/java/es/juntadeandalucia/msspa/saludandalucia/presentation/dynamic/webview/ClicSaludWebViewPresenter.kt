package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.webview

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebViewPresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class ClicSaludWebViewPresenter(val sessionBus: SessionBus) : BaseWebViewPresenter<ClicSaludWebViewContract.View>(),
    ClicSaludWebViewContract.Presenter {

    override fun onCreate() {
        sessionBus.buildSessionUser()?.let {
            view.loadToken(it.msspaAuthenticationEntity.accessToken)
        }
    }

    override fun onViewCreated(navigationEntity: NavigationEntity) {
        view.apply {
            setupView(navigationEntity.title)
            sessionBus.buildSessionUser()?.let {
                loadUrl(navigationEntity.target, Consts.AUTHORIZATION to it.msspaAuthenticationEntity.accessToken)
            }
        }
    }
}
