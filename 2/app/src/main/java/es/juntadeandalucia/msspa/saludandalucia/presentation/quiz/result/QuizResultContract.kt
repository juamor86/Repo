package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.result

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class QuizResultContract {

    interface View : BaseContract.View {
        fun setupView()
        fun closeView()
        fun showResult(quizResultEntity: QuizResultEntity)
        fun showAppointment(appointment: AppointmentEntity)
        fun showCancelAppointmentDialog()
        fun showAppointmentCanceledDialog()
        fun showErrorAppointmentCanceledDialog()
        fun addAppointmentToCalendar(appointment: AppointmentEntity)
        fun hideAppointment()
    }

    interface Presenter : BaseContract.Presenter {
        fun onAcceptButtonClicked()
        fun onAddAppointmentToCalendar()
        fun onCancelAppointment()
        fun onConfirmCancelAppointment()
        fun setupView(quizResultEntity: QuizResultEntity?)
    }
}
