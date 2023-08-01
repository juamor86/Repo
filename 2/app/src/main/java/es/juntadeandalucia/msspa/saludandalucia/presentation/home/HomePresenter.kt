package es.juntadeandalucia.msspa.saludandalucia.presentation.home

import android.content.Context
import android.view.View
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.DynamicUIBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicHomeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import timber.log.Timber

class HomePresenter(
    private val getAppsUseCase: GetAppsUseCase,
    private val getFeaturedUseCase: GetFeaturedUseCase,
    private val getFirstAccess: GetFirstAccessUseCase,
    private val saveFirstAccess: SetFirstAccessUseCase,
    private val validateContraindicationUseCase: ValidateContraindicationUseCase,
    private val sessionBus: SessionBus,
    private val dynamicUIBus: DynamicUIBus
) :
    BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    lateinit var token: String
    private lateinit var sections: Array<String>
    private var checkedFileValue: Boolean = false

    override fun onCreate(sections: Array<String>) {
        this.sections = sections
    }

    private fun onUserLogged(userLogged: MsspaAuthenticationUserEntity) {
        view.showUserLoggedIcon(userLogged.initials)
    }

    private fun onUserLoggedOut() {
        view.showUserNotLoggedIcon()
    }

    override fun onViewCreated(isHuawei: Boolean) {
        getDynamicHome()
        view.apply {
            setupAppsRecycler()
            setupListener()
        }
        getApps(isHuawei)
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.HOME_SCREEN_ACCESS

    override fun checkUserLogged() {
        sessionBus.execute(
            onNext = { session ->
                if (session.isUserAuthenticated()) {
                    onUserLogged(session.msspaAuthenticationEntity!!.msspaAuthenticationUser!!)
                } else {
                    onUserLoggedOut()
                }
            },
            onError = {
                onUserLoggedOut()
                Timber.e(it)
            }
        )
    }

    private fun getDynamicHome(strategy: Strategy = Strategy.NETWORK) {
        dynamicUIBus.getHome(
            onSuccess = {
                buildDynamicHome(it)
            }, onError = {
                Timber.e(it)
                if (checkedFileValue) {
                    getDynamicHome(Strategy.MOCK)
                } else {
                    checkedFileValue = true
                    getDynamicHome(Strategy.FILE)
                }
            }, strategy = strategy
        )
    }

    private fun buildDynamicHome(dynamicHomeEntity: DynamicHomeEntity) {
        view.buildDynamicHome(dynamicHomeEntity)
    }

    override fun onAppClicked(app: AppEntity, itemView: View) {
        view.navigateToAppDetail(app, itemView)
    }

    override fun onAppsButtonPressed() {
        view.navigateToApps()
    }

    private fun getApps(isHuawei: Boolean) {
        getAppsUseCase.param(isHuawei).execute(
            onSuccess = {
                view.apply { showApps(it) }
            },
            onError = {
                Timber.e(it)
                view.apply { showErrorApps() }
            }
        )
    }

    override fun onResume(token: String?) {
        token?.let {
            this@HomePresenter.token = token
            if (getFirstAccess.param(Consts.PREF_FIRST_ACCESS_TO_VALIDATE_CERTIFICATE).execute()) {
                view.showValidateCertificateOnboarding()
                saveFirstAccess
                    .param(Consts.PREF_FIRST_ACCESS_TO_VALIDATE_CERTIFICATE)
                    .execute(
                        onComplete = {},
                        onError = { Timber.e(it) }
                    )
            } else {
                validateToken()
            }
        }

    }

    override fun onPause() {
        view.clearArguments()
    }

    override fun onDynamicElementClicked(elem: DynamicElementEntity) {
        with(elem) {
            navigation.title = title.text
            view.apply {
                sendEvent(
                    Consts.Analytics.DYNAMIC_SCREEN_SCREEN_ACCESS + Utils.getDynamicScreenParam(
                        navigation.target
                    )
                )
                handleNavigation(navigation)
            }

        }
    }

    private fun validateToken() {
        validateContraindicationUseCase.paramToken(token).execute(
            onSuccess = { user ->
                if (user.isOk) {
                    view.showValidCovidCert(user)
                }
            },
            onError = {
                Timber.i(it, "Error validating certificate")
            }
        )
    }

    override fun onDismissedCertificateOnBoardingDialog() {
        validateToken()
    }

    override fun unsubscribe() {
        getAppsUseCase.clear()
        getFeaturedUseCase.clear()
        saveFirstAccess.clear()
        validateContraindicationUseCase.clear()
        view.hideLoading()
    }
}
