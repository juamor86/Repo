package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedContract

class NewMonitoringContract {

    interface View : BaseContract.View {
        fun setupView(questionnaire: QuestionnaireEntity)
        fun startQuiz()
        fun closeView()
        fun enableSendButton()
        fun disableSendButton()
        fun removeScrollListener()
        fun showConfirmDialog()
        fun setupQuestionnaire(
            questionnaire: QuestionnaireEntity,
            questionsList: MutableList<QuestionEntity>,
            responseSelectedListener: QuestionnaireController
        )
        fun animateView()
        fun showQuestion(position: Int)
        fun hideQuestion(position: Int)
        fun scrollTo(scrollPosition: Int)
        fun focusElement(position: Int)
        fun showSendAnswersError()
        fun showSendAnswersSuccess(detailProgram: MonitoringListEntity.QuestFilledEntity, title: String, id: String)
        fun informQuestionResponseNotValid()
        fun informQuestionResponseLessMin()
        fun informQuestionResponseMoreMax()
        fun setQuestionOk(position: Int)
        fun setQuestionError(position: Int)
        fun onBackPressed()
    }

    interface Presenter : LoggedContract.Presenter {
        fun onStop()
        fun onSendButtonClicked()
        fun sendQuestionnaireAnswer()
        fun onCreate(item: QuestionnaireEntity)
    }
}
