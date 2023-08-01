package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.NewDynQuestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendDynQuestAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendNewDynQuestAnswerData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicQuestionnairesRepository
import io.reactivex.Single

class DynamicQuestionnairesRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    DynamicQuestionnairesRepository {

    override fun getDynamicQuestList(questionnaireId: String, page: Int): Single<DynQuestListData> =
        msspaApi.getDynQuestList(questionnaireId = questionnaireId, page = page.toString())

    override fun getNewDynamicQuest(id: String): Single<NewDynQuestData> =
        msspaApi.getNewDynQuest(id = id)

    override fun sendQuestionaireAnswers(answers: SendNewDynQuestAnswerData): Single<SendDynQuestAnswersResponseData> =
        msspaApi.sendNewDynQuestAnswer(answers = answers)
}