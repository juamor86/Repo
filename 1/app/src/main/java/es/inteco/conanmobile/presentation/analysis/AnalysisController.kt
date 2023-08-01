package es.inteco.conanmobile.presentation.analysis

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx3.ReactiveNetwork
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.domain.usecases.analisys.BaseAnalysisUseCase
import es.inteco.conanmobile.domain.usecases.analisys.ListAppsUseCase
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.utils.AnalysisConsts
import es.inteco.conanmobile.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import kotlin.reflect.full.primaryConstructor

/**
 * Analysis controller
 *
 * @property context
 * @constructor Create empty Analysis controller
 */
class AnalysisController(
    val context: Context
) {
    private var isStopping: Boolean = false
    private val appsModules = mutableSetOf<String>()
    private lateinit var observer: Disposable
    private var totalItems: Int = 0
    private lateinit var onFinishAnalysis: (result: AnalysisResultEntity) -> Unit
    lateinit var result: AnalysisResultEntity
    private lateinit var view: AnalysisView
    private val launchedItems = mutableSetOf<BaseAnalysisUseCase>()
    var analysisEntity: AnalysisEntity? = null
    private val useCases = hashMapOf<String, BaseAnalysisUseCase>()
    private var items: Int = 0
    private lateinit var progress: AnalysisProgress

    /**
     * Start
     *
     * @param view
     * @param analysisEntity
     * @param onFinishAnalysis
     * @receiver
     */
    fun start(
        view: AnalysisView,
        analysisEntity: AnalysisEntity,
        onFinishAnalysis: (result: AnalysisResultEntity) -> Unit
    ) {
        if (!Utils.checkInternet(context)) {
            view.showAlertNoNetwork()
            return
        }
        this.view = view
        this.analysisEntity = analysisEntity
        this.onFinishAnalysis = onFinishAnalysis
        result = AnalysisResultEntity(analysisEntity)
        items = 0
        progress = AnalysisProgress(
            analysisEntity.deviceModules.size,
            analysisEntity.applicationModules.size,
            analysisEntity.systemModules.size
        )
        isStopping = false

        listenNetwork()
        executeAllAnalysis()
    }

    private fun listenNetwork() {
        observer = ReactiveNetwork.observeNetworkConnectivity(context).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { connectivity ->
                if (!connectivity.available() && !isStopping) {
                    stopAnalysis()
                    view.showAlertNoNetwork()
                }
            }
    }

    private fun executeAllAnalysis() {
        initUseCases()
        totalItems = useCases.size
        view.setProgressMax(progress.total)
        useCases.values.forEach {
            analyseItem(it)
        }
    }

    private fun analyseItem(item: BaseAnalysisUseCase) {
        item.run {
            launchedItems.add(this)
            execute(onSuccess = {
                cleanUseCase(this)
                processResult(it)
            }, onError = {
                Timber.e(it, "Error when analyzing: %s", item.analysisItem.code)
                cleanUseCase(this)
                processResult(ModuleResultEntity(item.analysisItem, true))
            })
        }
    }

    private fun constructUseCase(item: ModuleEntity) {
        var useCase: BaseAnalysisUseCase? = null
        AnalysisConsts.getUseCaseClass(item.code)?.let {
            useCase = (Class.forName(it).kotlin.primaryConstructor!!.call(
                context, item, result
            ) as BaseAnalysisUseCase?)!!
            useCases[item.code] = useCase as BaseAnalysisUseCase
        }

        if (item.type == ModuleEntity.AnalysisType.APPLICATION) {
            appsModules.add(item.code)
        }
        if (useCase is ListAppsUseCase) {
            (useCase as ListAppsUseCase).appsModules = appsModules
        }
    }

    private fun cleanUseCase(useCase: BaseAnalysisUseCase) {
        launchedItems.remove(useCase)
        useCase.clear()
    }

    /**
     * Is analysis finished
     *
     * @return
     */
    fun isAnalysisFinished(): Boolean {
        return launchedItems.size == 0
    }

    private fun processResult(itemResult: ModuleResultEntity) {
        when (itemResult.item.type) {
            ModuleEntity.AnalysisType.SETTING -> {
                if (itemResult.notOk) {
                    items++
                    view.setDeviceWarnings(items)
                }
                result.deviceItems.add(itemResult)
                progress.deviceItemFinished()
            }
            ModuleEntity.AnalysisType.APPLICATION -> {
                if (itemResult.notOk) {
                    view.setAppWarnings(result.appsItems.filter { applicationEntity -> applicationEntity.isMalicious == 1 || applicationEntity.criticalPermissions.isNotEmpty() }.size)
                }
                progress.appsItemFinished()
            }
            ModuleEntity.AnalysisType.SYSTEM -> {
                if (itemResult.notOk) {
                    view.setSystemWarnings(result.systemItems.size)
                }
                progress.systemItemFinished()
            }
        }
        view.updateProgress(progress.currentProgress)
        if (launchedItems.size == 0) {
            finishAnalysis()
        }
    }

    private fun finishAnalysis() {
        onFinishAnalysis(result)
        observer.dispose()
        launchedItems.clear()
        totalItems = 0
        useCases.clear()
    }

    /**
     * Stop analysis
     *
     */
    fun stopAnalysis() {
        isStopping = true
        launchedItems.forEach {
            it.clear()
        }
        launchedItems.clear()
        totalItems = 0
        useCases.clear()
    }

    /**
     * Execute action for
     *
     * @param item
     * @param resultView
     */
    fun executeActionFor(item: ModuleEntity, resultView: AnalysisResultView) {
        getUseCase(item)?.lunchAction(resultView)
    }

    private fun getUseCase(module: ModuleEntity): BaseAnalysisUseCase? {
        if (useCases.isEmpty()) {
            initUseCases()
        }
        return useCases[module.code]
    }

    private fun initUseCases() {
        analysisEntity?.apply {
            deviceModules.plus(applicationModules).plus(systemModules).onEach {
                constructUseCase(it)
            }
        }
    }

    private class AnalysisProgress(deviceItems: Int, appsItems: Int, systemItems: Int) {
        val total = (deviceItems + appsItems + systemItems) * 100
        var currentProgress = 0

        private val factors = listOf(deviceItems, appsItems, systemItems).filter { it > 0 }
        private val weight: Double = (total.toDouble() / factors.size)
        private val devicesFactor: Double = weight / deviceItems
        private val appsFactor: Double = weight / appsItems
        private val systemFactor: Double = weight / systemItems

        /**
         * Device item finished
         *
         */
        fun deviceItemFinished() {
            currentProgress += devicesFactor.toInt()
        }

        /**
         * Apps item finished
         *
         */
        fun appsItemFinished() {
            currentProgress += appsFactor.toInt()
        }

        /**
         * System item finished
         *
         */
        fun systemItemFinished() {
            currentProgress += systemFactor.toInt()
        }

    }
}