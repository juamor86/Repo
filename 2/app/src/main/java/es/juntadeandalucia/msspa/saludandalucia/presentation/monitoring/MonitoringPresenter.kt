package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.NoMonitoringProgramException
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetMonitoringUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class MonitoringPresenter(
    private val getMonitoringUseCase: GetMonitoringUseCase
) :
    BasePresenter<MonitoringContract.View>(), MonitoringContract.Presenter {

    override fun getScreenNameTracking(): String? = Consts.Analytics.FOLLOWUP_MEASUREMENTS_ACCESS

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.showLoading()
        getMonitoringUseCase.execute(
            onSuccess = {
                view.apply {
                    hideLoading()
                    setupView(it)
                }
            },
            onError = {
                view.hideLoading()
                Timber.e(it)
                when (it) {
                    is HttpException -> {
                        when (it.code()) {
                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                view.showErrorView()
                            }
                            HttpURLConnection.HTTP_BAD_GATEWAY -> {
                                view.showErrorDialogAndBack()
                            }
                            else -> handleUnauthorizedException(
                                exception = it,
                                action = { view.showErrorDialogAndBack() }
                            )
                        }
                    }
                    is NoMonitoringProgramException -> {
                        view.showNotMonitoringMessage()
                    }
                    else -> {
                        view.showErrorDialogAndBack()
                    }
                }
            }
        )
    }

    override fun onProgramClicked(item: MonitoringEntity.MonitoringEntry, itemView: View) {
        if (item.type == Consts.TYPE_MONITORING_PROGRAM) {
            view.navigateToProgram(item)
        } else {
            view.navigateToMeasurements()
        }
    }

    override fun unsubscribe() {
        getMonitoringUseCase.clear()
    }

}
