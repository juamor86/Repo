package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.NewMonitoringProgramData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.MonitoringRepository
import io.reactivex.Single

class MonitoringRepositoryNetworkImpl(private val msspaApi: MSSPAApi, private val session: Session) :
    MonitoringRepository {

    override fun getMonitoring(): Single<MonitoringData> =
        msspaApi.getMonitoring()

    override fun getMonitoringList(id: String, page: Int): Single<MonitoringListData> =
        msspaApi.getMonitoringList(questionnaireId= id, page = page.toString())

    override fun getNewMonitoring(id: String): Single<NewMonitoringProgramData> =
        msspaApi.getNewMonitoring(id = id)

    override fun sendQuestionaireAnswers(answers: SendNewMonitoringAnswerData): Single<SendMonitoringAnswersResponseData> =
        msspaApi.sendNewMonitoringAnswer(answers = answers)
}
