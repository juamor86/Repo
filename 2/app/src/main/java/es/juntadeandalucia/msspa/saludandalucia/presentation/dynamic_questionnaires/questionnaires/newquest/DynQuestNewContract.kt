package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest

import android.graphics.Bitmap
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.LoggedContract
import java.io.File

class DynQuestNewContract {

    interface View : BaseContract.View {
        fun setupView(questionnaire: DynQuestionnaireEntity, title: String)
        fun startQuiz()
        fun closeView()
        fun enableSendButton()
        fun disableSendButton()
        fun removeScrollListener()
        fun showConfirmDialog()
        fun showConFirmDialogLoggedout()
        fun setupQuestionnaire(
            questionnaire: DynQuestionnaireEntity,
            questionsList: MutableList<DynQuestionEntity>,
            responseSelectedListener: DynQuestNewController
        )
        fun onBackPressed()
        fun animateView()
        fun showQuestion(position: Int)
        fun hideQuestion(position: Int)
        fun scrollTo(scrollPosition: Int)
        fun focusElement(position: Int)
        fun showSendAnswersError()
        fun showSendAnswersSuccess(detailProgram: DynQuestListEntity.QuestFilledEntity, title: String, id: String)
        fun informQuestionResponseNotValid(position: Int)
        fun informQuestionResponseLessMin()
        fun informQuestionResponseMoreMax()
        fun informQuestionChoiceResponseMoreMax()
        fun informQuestionUrlFormatNotValid(position: Int)
        fun setQuestionOk(position: Int)
        fun setQuestionError(position: Int)
        fun requestPermissions()
        fun showAttachmentResponse(file: File, position: Int, size: Int)
        fun showFilesAreTooLarge()
        fun showServiceNotAvailable()
        fun attachmentQuestionChanged()
        fun showHelpButton()
        fun hidenHelpButton()
        fun navigateToHelp(questionnaireEntityHelp: DynQuestionnaireEntity)
    }

    interface Presenter : LoggedContract.Presenter {
        fun onStop()
        fun onSendButtonClicked()
        fun sendQuestionnaireAnswer()
        fun onCreate(item: DynQuestionnaireEntity?, title: String?, quizId: String)
        fun onAttachmentClick(dynQuestionEntity: DynQuestionEntity)
        fun attachmentCompleted(file: File)
        fun onHelpCLicked()
    }
}

