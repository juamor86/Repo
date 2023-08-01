package es.inteco.conanmobile.presentation.splash

import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.base.BaseContract

class SplashContract {
    interface View : BaseContract.View {
        fun checkInternetConnectivity()
        fun navigateToHomeScreen(message: MessageEntity)
        fun navigateToLegalScreen(message: MessageEntity)
        fun showNoConfigurationScreen()
        fun showTermsAndConditionsScreen()
        fun showSplashScreen()
        fun showNoInternetScreen()
        fun initButtons()
        fun showNoInternetConnectionDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(idDevice: String)
        fun onAvailableInternetConnectivity()
        fun onUnavailableInternetConnectivity()
        fun onAcceptButtonClicked()
        fun delayAndNavigateHome()
        fun onLegalClicked()
    }
}