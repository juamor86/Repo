package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class QuizContract {

    interface View : BaseContract.View {
        fun setupView()
        fun showQuizNotAvailable(nextTry: String)
        fun startQuiz(questions: List<QuizQuestionEntity>)
        fun closeView()
        fun enableSendButton()
        fun navigateToResult(quizResultEntity: QuizResultEntity)
        fun removeScrollListener()
        fun showConfirmDialog()
        fun showAppointment(appointment: AppointmentEntity)
        fun showCancelAppointmentDialog()
        fun showAppointmentCanceledDialog()
        fun showErrorAppointmentCanceledDialog()
        fun addAppointmentToCalendar(appointment: AppointmentEntity)
        fun dismissAppointmentDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun onStop()
        fun onResponseSelected(questionEntity: QuizQuestionEntity, response: String)
        fun onAcceptButtonClicked()
        fun onSendButtonClicked()
        fun sendQuizResponses()
        fun closeQuiz()
        fun onAddAppointmentToCalendar()
        fun onCancelAppointment()
        fun onConfirmCancelAppointment()
        fun setupView(user: QuizUserEntity)
    }
}
