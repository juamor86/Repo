package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasureHelperEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetMeasureHelperUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetMeasureSectionUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import timber.log.Timber

class MeasurementsPresenter(
    private val getMeasureSectionUseCase: GetMeasureSectionUseCase,
    private val getMeasureHelperUseCase: GetMeasureHelperUseCase
) :
    BasePresenter<MeasurementsContract.View>(), MeasurementsContract.Presenter {

    private lateinit var measuresList: MutableList<MeasurementSectionEntity>

    override fun getScreenNameTracking(): String? = Consts.Analytics.MEASUREMENTS_SCREEN_ACCESS

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.showLoading()
        getMeasureSectionUseCase.execute(
            onSuccess = {
                measuresList = it.toMutableList()
                if (measuresList.isEmpty()) {
                    view.showNoMeasuresText()
                } else {
                    view.apply {
                        animateView()
                        hideLoading()
                        initView(measuresList)
                    }
                    getMeasures(it[0].currentPage, it[0].lastPage)
                }
                getHelpers()
            },
            onError = {
                view.apply {
                    hideLoading()
                    animateView()
                }
                when(it){
                    is  IndexOutOfBoundsException -> view.showNoMeasuresText()
                    else -> handleUnauthorizedException(it)
                }
                Timber.e(it)
            })
    }

    override fun onSectionClicked(measure: MeasurementSectionEntity) {
        view.apply {
            sendEvent(Consts.Analytics.MEASUREMENTS_FILTER_ACCESS + Utils.unaccent(measure.title))
            onSectionClicked(measure)
        }
    }

    override fun onFilterClicked(measure: String) {
        Utils.unaccent(measure)
        view.apply {
            sendEvent(Consts.Analytics.MEASUREMENTS_SPECIFIC_ACCESS + Utils.unaccent(measure))
            onFilterClicked(measure)
        }
    }

    override fun onGraphicButtonPressed(measure: MeasurementSectionEntity) {
        view.apply {
            sendEvent(Consts.Analytics.MEASUREMENTS_DETAIL_GRAPHIC_ACCESS + Utils.unaccent(measure.title))
            onGraphicButtonPressed(measure)
        }
    }

    override fun onListButtonPressed(measureTitle: String) {
        view.apply {
            sendEvent(Consts.Analytics.MEASUREMENTS_DETAIL_LIST_ACCESS + Utils.unaccent(measureTitle))
            onListButtonPressed()
        }
    }

    override fun getDetailInfo(measure: MeasurementSectionEntity) {
        view.apply {
            sendEvent(Consts.Analytics.MEASUREMENTS_HELP_ACCESS + Utils.unaccent(measure.title))
            showDetailInfo(measure.helpText)
        }
    }

    private fun getHelpers() =
        getMeasureHelperUseCase.execute(
            onSuccess = { setHelpers(it) },
            onError = { Timber.e(it) }
        )

    private fun getMeasures(currentPage: Int, lastPage: Int) {
        if (currentPage != lastPage) {
                    getMeasureSectionUseCase.params(currentPage +1)

                    getMeasureSectionUseCase.execute(
                        onSuccess = {
                            if (!it.isNullOrEmpty()){
                                setMeasures(it)
                                getMeasures(it[0].currentPage, it[0].lastPage)
                            }
                        },
                        onError = { Timber.e(it) }
                    )
        }
    }

    private fun setMeasures(measures: List<MeasurementSectionEntity>) {

        val measureAux: MutableList<MeasurementSectionEntity> = mutableListOf()

        for (newMeasure in measures) {
            if (filterByTitle(newMeasure.title, measuresList)) {
                measuresList.find { it.title == newMeasure.title }?.values?.addAll(newMeasure.values)
            } else {
                measureAux.add(newMeasure)
            }
        }
        measuresList.addAll(measureAux)
        view.reDrawAdapter(measuresList)
    }

    private fun filterByTitle(
        title: String,
        list: List<MeasurementSectionEntity>
    ): Boolean =

        list.any {
            it.title == title
        }

    private fun setHelpers(list: List<MeasureHelperEntity>) {
        list.map {
            for (measure in measuresList) {
                if (it.groupName == measure.title) {
                    measure.helpText = it.helpText
                    measure.range = it.range
                }
            }
        }

        view.reDrawAdapter(measuresList)
    }

    override fun unsubscribe() {
        getMeasureHelperUseCase.clear()
        getMeasureSectionUseCase.clear()
    }
}
