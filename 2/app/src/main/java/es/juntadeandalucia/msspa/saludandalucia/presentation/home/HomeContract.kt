package es.juntadeandalucia.msspa.saludandalucia.presentation.home

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicHomeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class HomeContract {

    interface View : BaseContract.View {
        fun setupAppsRecycler()
        fun showApps(apps: List<AppEntity>)
        fun showErrorApps()
        fun navigateToAppDetail(app: AppEntity, itemView: android.view.View)
        fun setupListener()
        fun navigateToApps()
        fun navigateToQuiz()
        fun navigateToWebview(linkMode: String)
        fun navigateToPreferences()
        fun navigateToCovidCertificate()
        fun navigateToGreenPass()
        fun navigateToExternalBrowser(linkMode: String)
        fun showValidCovidCert(user: UECovidCertEntity)
        fun showInvalidCovidCert()
        fun showValidateCertificateOnboarding()
        fun clearArguments()
        fun buildDynamicHome(dynamicHomeEntity: DynamicHomeEntity)
        fun handleNavigation(navigation: NavigationEntity)
        fun showUserLoggedIcon(initials: String)
        fun showUserNotLoggedIcon()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(sections: Array<String>)
        fun onViewCreated(isHuawei: Boolean)
        fun onResume(token: String?)
        fun onDismissedCertificateOnBoardingDialog()
        fun onPause()
        fun onDynamicElementClicked(elem: DynamicElementEntity)
        fun onAppClicked(app: AppEntity, itemView: android.view.View)
        fun onAppsButtonPressed()
        fun checkUserLogged()
    }
}
