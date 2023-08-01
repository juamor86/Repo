package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.NewMonitoringProgramData
import io.reactivex.Single

interface MonitoringRepository {

    fun getMonitoring(): Single<MonitoringData>

    fun getMonitoringList(questionnaireId: String ,page: Int): Single<MonitoringListData>

    fun getNewMonitoring(id: String): Single<NewMonitoringProgramData>

    fun sendQuestionaireAnswers(answers: SendNewMonitoringAnswerData): Single<SendMonitoringAnswersResponseData>
}
