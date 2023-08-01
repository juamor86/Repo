package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.NewDynQuestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendDynQuestAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendNewDynQuestAnswerData
import io.reactivex.Single

interface DynamicQuestionnairesRepository {

    fun getDynamicQuestList(questionnaireId: String, page: Int): Single<DynQuestListData>

    fun getNewDynamicQuest(id: String): Single<NewDynQuestData>

    fun sendQuestionaireAnswers(answers: SendNewDynQuestAnswerData): Single<SendDynQuestAnswersResponseData>
}