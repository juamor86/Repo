package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.NewDynQuestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendDynQuestAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendNewDynQuestAnswerData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicQuestionnairesRepository
import io.reactivex.Single

class DynamicQuestionnairesRepositoryMockImpl : DynamicQuestionnairesRepository {

    override fun getDynamicQuestList(questionnaireId: String, page: Int): Single<DynQuestListData> {
        TODO("Not yet implemented")
    }

    override fun getNewDynamicQuest(id: String): Single<NewDynQuestData> {
        TODO("Not yet implemented")
    }

    override fun sendQuestionaireAnswers(answers: SendNewDynQuestAnswerData): Single<SendDynQuestAnswersResponseData> {
        TODO("Not yet implemented")
    }
}