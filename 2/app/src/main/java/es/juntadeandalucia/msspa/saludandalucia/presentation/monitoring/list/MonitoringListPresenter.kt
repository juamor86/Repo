package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetMonitoringListUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNewMonitoringUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class MonitoringListPresenter(
    private val getNewMonitoringUseCase: GetNewMonitoringUseCase,
    private val getMonitoringListUseCase: GetMonitoringListUseCase
) :
    BasePresenter<MonitoringListContract.View>(),
    MonitoringListContract.Presenter {

    private lateinit var entry: MonitoringEntity.MonitoringEntry
    private var newMonitoringEntity: QuestionnaireEntity? = null
    private var monitoringList: MonitoringListEntity = MonitoringListEntity(mutableListOf())

    override fun onCreate(item: MonitoringEntity.MonitoringEntry) {
        this.entry = item
        view.setupList()
    }

    override fun onViewCreated() {
        view.apply {
            sendEvent(Consts.Analytics.FOLLOWUP_LIST_ACCESS)
            animateView()
            setTitle(entry.title)
        }

        if (monitoringList.questsFilled.isNotEmpty()) {
            view.showList(monitoringList = monitoringList)
        } else {
            loadList()
        }
        showSubtitleAndButton()
    }

    private fun showSubtitleAndButton() {
        newMonitoringEntity?.apply {
            view.showNewMonitoringButton()
            view.setSubtitle(purpose)
        } ?: getNewMonitoring()
    }

    private fun getNewMonitoring() {
        getNewMonitoringUseCase.setId(entry.id).execute(
            onSuccess = {
                newMonitoringEntity = it
                showSubtitleAndButton()
            },
            onError = {
                view.hideLoading()
                Timber.e(it)
            }
        )
    }

    private fun loadList() {
        view.showLoading()
        monitoringList.questsFilled.clear()
        view.showList(monitoringList)
        getMonitoringListUseCase.setId(entry.id).execute(
            onNext = {
                with(monitoringList.questsFilled) {
                    addAll(size, it.questsFilled)
                    view.apply {
                        hideLoading()
                        if (isNotEmpty()) {
                            showList(monitoringList)
                        } else {
                            view.showNoMonitoring()
                        }
                    }
                }
            },
            onError = {
                view.hideLoading()
                handleUnauthorizedException(
                    exception = it,
                    action = { view.showErrorDialogAndBack() })

                Timber.e(it)
            },
            onCompleted = {}
        )

    }

    override fun onRefresh() {
        loadList()
    }


    override fun onItemClicked(
        detailProgram: MonitoringListEntity.QuestFilledEntity,
        itemView: View
    ) {
        view.navigateToDetail(detailProgram, entry.title, entry.id)
    }

    override fun onNewProgramClicked() {
        view.hideLoading()
        view.navigateToNewProgram(newMonitoringEntity!!)
    }

    override fun unsubscribe() {
        getMonitoringListUseCase.clear()
    }
}
