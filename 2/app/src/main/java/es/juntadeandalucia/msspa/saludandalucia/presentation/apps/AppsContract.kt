package es.juntadeandalucia.msspa.saludandalucia.presentation.apps

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class AppsContract {

    interface View : BaseContract.View {
        fun initializeAppsRecycler()
        fun showApps(apps: List<AppEntity>)
        fun downloadApp(app: AppEntity)
        fun openApp(app: AppEntity)
        fun showFilteredApps(listApps: List<AppEntity>)
        fun setupSearchView()
        fun showAppDetails(app: AppEntity, itemView: android.view.View)
        fun hideRefreshing()
        fun showNoMatchAppsView()
    }

    interface Presenter : BaseContract.Presenter {

        fun onResume(isHuawei: Boolean)
        fun refreshApps(isHuawei: Boolean)
        fun onAppClicked(app: AppEntity, itemView: android.view.View)
        fun onButtonPressed(context: Context, app: AppEntity)
        fun filterList(text: String)
    }
}
