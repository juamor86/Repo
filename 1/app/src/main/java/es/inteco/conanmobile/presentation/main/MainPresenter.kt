package es.inteco.conanmobile.presentation.main

import android.content.Context
import android.content.Intent
import android.provider.Settings
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.SynchronousUseCase
import es.inteco.conanmobile.domain.entities.*
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import es.inteco.conanmobile.presentation.base.BasePresenter
import es.inteco.conanmobile.presentation.main.MainContract.Presenter
import es.inteco.conanmobile.presentation.main.MainContract.View
import es.inteco.conanmobile.utils.ApplicationPackageUtils
import es.inteco.conanmobile.utils.Consts
import es.inteco.conanmobile.utils.Utils
import timber.log.Timber

/**
 * Main presenter
 *
 * @property getConfigurationUseCase
 * @property getDefaultAnalysisUseCase
 * @property existLastAnalysisUseCase
 * @property getLastAnalysisUseCase
 * @property analysisController
 * @property checkPendingWarningsUseCase
 * @property getLastAlertAnalysisUseCase
 * @property saveLastAlertAnalysisUseCase
 * @constructor Create empty Main presenter
 */
class MainPresenter(
    private val getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>,
    private val getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>,
    private val existLastAnalysisUseCase: SingleUseCase<Void, Boolean>,
    private val getLastAnalysisUseCase: SingleUseCase<Void, AnalysisResultEntity>,
    private val analysisController: AnalysisController,
    private val checkPendingWarningsUseCase: SingleUseCase<Void, PendingWarningsEntity>,
    private val getLastAlertAnalysisUseCase: SynchronousUseCase<Void, Long>,
    private val saveLastAlertAnalysisUseCase: SynchronousUseCase<Long, Unit>
) : BasePresenter<View>(), Presenter {

    lateinit var lastAnalysis: AnalysisResultEntity
    lateinit var message: MessageEntity
    lateinit var defaultAnalysis: AnalysisEntity

    override fun onCreate() {
        getDefaultAnalysis()
        getConfiguration()
        getPendingWarnings()
    }

    private fun getConfiguration() {
        getConfigurationUseCase.execute(onSuccess = {
            this.message = it.message
            checkLastAnalysis()
        }, onError = { Timber.e(it) })
    }

    private fun getPendingWarnings() {
        checkPendingWarningsUseCase.execute(onSuccess = {
            view.showPendingWarnings(it.message.haveNotifications)
        }, onError = {
            Timber.e("Error checking pending warnings - ${it.message}")
        })
    }

    private fun checkLastAnalysis() {
        existLastAnalysisUseCase.execute(onSuccess = { exist ->
            view.showViewLastAnalysis(exist)
            if (exist) {
                getLastAnalysisUseCase.execute(onSuccess = {
                    lastAnalysis = it
                    checkLastAnalysisTime()
                    checkRecommendedTime()
                }, onError = { Timber.e(it) })
            }
        }, onError = {
            view.showViewLastAnalysis(false)
        })
    }

    private fun checkLastAnalysisTime() {
        var timeAnalysis: Long? = null
        message.administration.forEach {
            if (it.key == Consts.TIME_BETWEEN_ANALYSIS) {
                timeAnalysis = it.value.toLong()
            }
        }
        timeAnalysis?.let {
            val diff = System.currentTimeMillis() - (lastAnalysis.date.time + it)
            if (diff < 0) {
                view.deactivateAnalysisUntil(-diff)
            }
        }
    }

    private fun checkRecommendedTime() {
        var recommendedTime: Long? = null
        message.administration.forEach {
            if (it.key == Consts.RECOMMENDED_TIME_BETWEEN_ANALYSIS) {
                recommendedTime = it.value.toLong()
            }
        }
        recommendedTime?.let {
            val diff = System.currentTimeMillis() - (lastAnalysis.date.time + it)
            val lastTime = getLastAlertAnalysisUseCase.execute()
            if (diff > 0 && (lastTime == 0L || (lastTime + Consts.ONE_DAY_MILLIS) < System.currentTimeMillis())) {
                view.showAlertRecommendedAnalysis()
                saveLastAlertAnalysisUseCase.execute(System.currentTimeMillis())
            }
        }
    }

    override fun onLastAnalysisClicked() {
        getLastAnalysisUseCase.execute(onSuccess = {
            analysisController.result = it
            analysisController.analysisEntity = it.analysisEntity
            view.navigateToResults(it)
        }, onError = {
            Timber.e(it, "Error getting last analysis")
            view.showErrorDialog()
        })
    }

    override fun onStartAnalysisClicked() {
        if (view.isLocationPermitted()) {
            navigateToAnalysis()
        } else {
            view.requestLocationPermission()
        }
    }

    private fun navigateToAnalysis() {
        view.navigateToAnalysis(defaultAnalysis)
    }

    override fun onLocationPermissionGranted() {
        navigateToAnalysis()
    }

    override fun goToDeviceConfiguration(ct: Context) {
        ct.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }

    override fun lunchWhatsapp(ct: Context) {
        ct.applicationContext?.let { applicationContext ->
            ApplicationPackageUtils.isAppAvailable(applicationContext, Consts.WHATSAPP_PACKAGE_NAME)
                .let { isAppAvailable ->
                    if (isAppAvailable) {
                        Utils.goToWhatsapp(ct)
                    } else {
                        view.showWarningIntentWhatsapp()
                    }
                }
        }
    }

    override fun onNavigateUp() {
        getDefaultAnalysis()
    }

    private fun getDefaultAnalysis() {
        getDefaultAnalysisUseCase.execute(
            onSuccess = {
                this@MainPresenter.defaultAnalysis = it
                view.initControlsWithDefaultAnalysis(it)
            },
            onError = { Timber.e(it) }
        )
    }

    override fun unsubscribe() {
        getConfigurationUseCase.clear()
    }

    override fun onLegalClicked() {
        view.navigateToLegalScreen(message)
    }

    override fun onHelpClicked() {
        view.navigateToHelpScreen()
    }
}