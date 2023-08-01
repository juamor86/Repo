package es.inteco.conanmobile.presentation.splash

import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.SynchronousUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import es.inteco.conanmobile.domain.usecases.*
import es.inteco.conanmobile.presentation.base.BasePresenter
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Splash presenter
 *
 * @property getConfigurationUseCase
 * @property setFirstAccessUseCase
 * @property getFirstAccessUseCase
 * @property saveDefaultAnalysisUseCase
 * @property getDefaultAnalysisUseCase
 * @property registerDeviceUseCase
 * @property saveDeviceRegisterUseCase
 * @property saveConfigurationUseCase
 * @constructor Create empty Splash presenter
 */
class SplashPresenter(
    private val getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>,
    private val setFirstAccessUseCase: CompletableUseCase<Void>,
    private val getFirstAccessUseCase: SynchronousUseCase<Void, Boolean>,
    private val saveDefaultAnalysisUseCase: CompletableUseCase<SaveDefaultAnalysisUseCase.Params>,
    private val getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>,
    private val registerDeviceUseCase: SingleUseCase<RegisterDeviceUseCase.Params, RegisteredDeviceEntity>,
    private val saveRegisteredDeviceUseCase: CompletableUseCase<SaveRegisteredDeviceUseCase.Params>,
    private val saveConfigurationUseCase: CompletableUseCase<SaveConfigurationUseCase.Params>
) : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    var configurationEntity: ConfigurationEntity? = null
    lateinit var idDevice: String
    var isInternetConnectivityAvailable = false

    override fun onCreate(idDevice: String) {
        this.idDevice = idDevice
        view.checkInternetConnectivity()
    }

    override fun onAvailableInternetConnectivity() {
        isInternetConnectivityAvailable = true
        checkConfigurationUseCase()
    }

    override fun onUnavailableInternetConnectivity() {
        isInternetConnectivityAvailable = false
        view.showNoInternetScreen()
    }

    private fun checkConfigurationUseCase() {
        view.showSplashScreen()
        registerDeviceUseCase.execute(
            params = RegisterDeviceUseCase.Params(idDevice),
            onSuccess = {
                saveRegisteredDeviceUseCase.execute(
                    params = SaveRegisteredDeviceUseCase.Params(it),
                    onComplete = {},
                    onError = { error -> Timber.e("Error registering device: ${error.message}") })
                getConfiguration(it)
            },
            onError = {
                view.showNoConfigurationScreen()
                Timber.e("registerDeviceUseCase: ${it.message}")
            }
        )
    }

    private fun getConfiguration(registeredDeviceEntity: RegisteredDeviceEntity) {
        getConfigurationUseCase
            .execute(GetConfigurationUseCase.Params(registeredDeviceEntity.message.key), onSuccess = { configuration ->
                saveConfigurationUseCase.execute(SaveConfigurationUseCase.Params(configuration),onComplete = {
                    Timber.d("Saved configuration")
                    configurationEntity = configuration
                    screenMode()
                    getDefaultAnalysisUseCase.execute(onSuccess = { defaultAnalysis ->
                        val analysis =
                            configuration.message.analysis.find { analysisEntity -> analysisEntity.id == defaultAnalysis.id }
                        if (analysis == null) {
                            // Default analysis is not available, so it takes the first analysis from configuration
                            configuration.message.analysis[0].let { firstAnalysis ->
                                saveDefaultAnalysisUseCase
                                    .execute(SaveDefaultAnalysisUseCase.Params(firstAnalysis),onComplete = {
                                        Timber.d("Saved default analysis with id ${firstAnalysis.id}")
                                    }, onError = {
                                        Timber.e("Error saving default analysis with id ${firstAnalysis.id}. Error: ${it.message}")
                                    })
                            }
                        }
                    }, onError = {
                        Timber.e("getDefaultAnalysisUseCase: ${it.message}")
                    })
                }, onError = {
                    Timber.e("Error saving configuration: ${it.message}")
                })
            }, onError = {
                view.showNoConfigurationScreen()
                Timber.e("getConfigurationUseCase: ${it.message}")
            })
    }

    private fun screenMode() {
        if (getFirstAccessUseCase.execute()) {
            view.showTermsAndConditionsScreen()
        } else {
            delayAndNavigateHome()
        }
    }

    override fun onAcceptButtonClicked() {
        setFirstAccessUseCase.execute(
            onComplete = {
                view.navigateToHomeScreen(configurationEntity!!.message)
            },
            onError = { Timber.e(it) }
        )
    }

    override fun delayAndNavigateHome() {
        Observable.timer(Consts.SPLASH_DELAY, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
            .map<Any> { view.navigateToHomeScreen(configurationEntity!!.message) }.subscribe()
    }

    override fun onLegalClicked() {
        view.navigateToLegalScreen(configurationEntity!!.message)
    }

    override fun unsubscribe() {
        getConfigurationUseCase.clear()
        setFirstAccessUseCase.clear()
        saveDefaultAnalysisUseCase.clear()
        getDefaultAnalysisUseCase.clear()
        registerDeviceUseCase.clear()
        saveRegisteredDeviceUseCase.clear()
    }
}

