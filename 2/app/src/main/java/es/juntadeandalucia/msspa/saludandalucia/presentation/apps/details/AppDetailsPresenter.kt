package es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class AppDetailsPresenter : BasePresenter<AppDetailsContract.View>(), AppDetailsContract.Presenter {

    private lateinit var appEntity: AppEntity

    override fun onCreateView(appEntity: AppEntity) {
        this.appEntity = appEntity
        view.apply {
            setUpView()
            showAppDetails(appEntity)
        }
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.APP_DETAIL_SCREEN_ACCESS

    override fun onButtonPressed() {
        val isAppInstalled = view.isAppInstalled(appEntity.packageName)
        view.apply {
            if (isAppInstalled) {
                openApp(appEntity.packageName)
            } else {
                downloadApp(appEntity.link)
            }
        }
    }

    override fun onResume() {
        view.checkAppInstalled(appEntity.packageName)
    }

    override fun onImageDetailsClick(URLimage: String, itemView: View) {
        view.openImageDetail(URLimage, itemView)
    }

    override fun unsubscribe() {
    }
}
