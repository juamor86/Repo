package es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class AppDetailsContract {

    interface View : BaseContract.View {
        fun setUpView()
        fun checkAppInstalled(packageName: String)
        fun showAppDetails(appEntity: AppEntity)
        fun openImageDetail(URLImage: String, itemView: android.view.View)
        fun isAppInstalled(packageName: String): Boolean
        fun openApp(packageName: String)
        fun downloadApp(linkApp: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun onButtonPressed()
        fun onResume()
        fun onImageDetailsClick(URLimage: String, itemView: android.view.View)
        fun onCreateView(appEntity: AppEntity)
    }
}
