package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.NewMonitoringProgramData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.MonitoringRepository
import io.reactivex.Single

class MonitoringRepositoryMockImpl(private val context: Context) : MonitoringRepository {

    override fun getMonitoring(): Single<MonitoringData> =
        TODO()

    override fun getMonitoringList(id: String, page: Int): Single<MonitoringListData> {
        TODO("Not yet implemented")
    }

    override fun getNewMonitoring(id: String): Single<NewMonitoringProgramData> =
        Single.just(
            NewMonitoringProgramData(
                title = "Titulo prueba"
            )
        )

    override fun sendQuestionaireAnswers(answers: SendNewMonitoringAnswerData): Single<SendMonitoringAnswersResponseData> {
        TODO("Not yet implemented")
    }
}
