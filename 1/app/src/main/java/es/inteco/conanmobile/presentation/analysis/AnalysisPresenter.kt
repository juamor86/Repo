package es.inteco.conanmobile.presentation.analysis

import es.inteco.conanmobile.domain.base.BehaviorSubjectUseCase
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.bus.LifecycleBus
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.entities.PostAnalysisResultEntity
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.domain.usecases.SaveAnalysisUseCase
import es.inteco.conanmobile.domain.usecases.analisys.PostAnalysisResultUseCase
import es.inteco.conanmobile.presentation.base.BasePresenter
import timber.log.Timber

/**
 * Analysis presenter
 *
 * @property controller
 * @property getConfigurationUseCase
 * @property getDefaultAnalysisUseCase
 * @property postAnalysisResultUseCase
 * @property saveAnalysisUseCase
 * @property existLastAnalysisUseCase
 * @property getLastAnalysisUseCase
 * @property lifecycleBus
 * @constructor Create empty Analysis presenter
 */
class AnalysisPresenter(
    private val controller: AnalysisController,
    private val getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>,
    private val getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>,
    private val postAnalysisResultUseCase: SingleUseCase<PostAnalysisResultUseCase.Params, PostAnalysisResultEntity>,
    private val saveAnalysisUseCase: CompletableUseCase<SaveAnalysisUseCase.Params>,
    private val existLastAnalysisUseCase: SingleUseCase<Void, Boolean>,
    private val getLastAnalysisUseCase: SingleUseCase<Void, AnalysisResultEntity>,
    private val lifecycleBus: BehaviorSubjectUseCase<LifecycleBus.LifecycleState>
) : BasePresenter<AnalysisContract.View>(), AnalysisContract.Presenter {

    private var previousAnalysis: AnalysisResultEntity? = null
    var isBackground: Boolean = false
    var bluetoothRequestTimes = 0

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
        view.checkBluetoothPermissions()
        lifecycleBus.execute(onNext = { state ->
            if (isBackground && state == LifecycleBus.LifecycleState.FOREGROUND) {
                checkAnalysisBackgroundFinished()
            }
            isBackground = state == LifecycleBus.LifecycleState.BACKGROUND
        }, onError = {
            Timber.e("Error getting app lifecycle")
        })
        getLastAnalysis()
    }

    override fun onPermissionsGranted() {
        startAnalysis()
    }

    override fun onPermissionsNotGranted() {
        bluetoothRequestTimes++
        if (bluetoothRequestTimes < 3) {
            view.checkBluetoothPermissions()
        } else {
            view.navigateBack()
        }
    }

    private fun getLastAnalysis() {
        if (existLastAnalysisUseCase.buildUseCase().blockingGet()) {
            previousAnalysis = getLastAnalysisUseCase.buildUseCase().blockingGet()
        }
    }

    private fun checkAnalysisBackgroundFinished() {
        if (!isBackground && controller.isAnalysisFinished()) {
            view.navigateToResults(controller.result, previousAnalysis)
        }
    }

    fun onFinishAnalysis(result: AnalysisResultEntity) {
        saveAnalysis(result)
        postAnalysisOnServer(result)
        view.apply {
            if (isBackground) {
                sendNotification()
            }
            navigateToResults(result, previousAnalysis)
        }
    }

    private fun saveAnalysis(result: AnalysisResultEntity) {
        saveAnalysisUseCase.execute(params = SaveAnalysisUseCase.Params(result), onComplete = {
            Timber.i("Analysis saved successfully")
        }, onError = {
            Timber.e("saveAnalysisResultUseCase ko: (${it.message})")
        })
    }

    private fun postAnalysisOnServer(result: AnalysisResultEntity) {
        getConfigurationUseCase.execute(onSuccess = { configuration ->
            getDefaultAnalysisUseCase.execute(onSuccess = {
                postAnalysisResultUseCase.execute(
                    PostAnalysisResultUseCase.Params(
                        result,
                        configuration.message.version,
                        it
                    ), onSuccess = {
                        Timber.i("Analysis posted successfully ${it.message}")
                    }, onError = {
                        Timber.e("postAnalysisResultUseCase ko: (${it.message})")
                    })
            }, onError = {
                Timber.e("getDefaultAnalysisUseCase ko: (${it.message})")
            })

        }, onError = {
            Timber.e("getConfigurationUseCase ko: (${it.message})")
        })
    }

    override fun onDestroy() {
        controller.stopAnalysis()
    }

    override fun startAnalysis() {
        getDefaultAnalysisUseCase.execute(
            onSuccess = {
                controller.start(view, it, this::onFinishAnalysis)
            },
            onError = { Timber.e(it) }
        )
    }
}