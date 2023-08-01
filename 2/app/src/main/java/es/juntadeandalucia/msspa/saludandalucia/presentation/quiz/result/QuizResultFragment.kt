package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.result

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_quiz_result.accept_btn
import kotlinx.android.synthetic.main.fragment_quiz_result.appointment_group
import kotlinx.android.synthetic.main.fragment_quiz_result.appointment_header_iv
import kotlinx.android.synthetic.main.fragment_quiz_result.quiz_result_tv
import kotlinx.android.synthetic.main.fragment_quiz_result.result_add_to_calendar_btn
import kotlinx.android.synthetic.main.fragment_quiz_result.result_appointment_center_tv
import kotlinx.android.synthetic.main.fragment_quiz_result.result_appointment_date_tv
import kotlinx.android.synthetic.main.fragment_quiz_result.result_appointment_task_tv
import kotlinx.android.synthetic.main.fragment_quiz_result.result_appointment_topic_tv
import kotlinx.android.synthetic.main.fragment_quiz_result.result_cancel_appointment_btn

class QuizResultFragment : BaseFragment(), QuizResultContract.View {

    @Inject
    lateinit var presenter: QuizResultContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_quiz_result

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.apply {
            setupView(arguments?.getParcelable(Consts.ARG_QUIZ_RESULT))
        }
    }

    //region - View methods
    override fun setupView() {
        accept_btn.setOnClickListener {
            presenter.onAcceptButtonClicked()
        }
    }

    override fun showResult(quizResultEntity: QuizResultEntity) {
        with(quizResultEntity) {
            appointment_header_iv.setImageDrawable(context?.getDrawable(headerResId))
            quiz_result_tv.text = result
        }
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    override fun showAppointment(appointment: AppointmentEntity) {
        appointment_group.visibility = View.VISIBLE
        with(appointment) {
            result_appointment_date_tv.text = date
            result_appointment_center_tv.text = center
            result_appointment_topic_tv.text = topic
            result_appointment_task_tv.text = task
        }
        result_cancel_appointment_btn.setOnClickListener { presenter.onCancelAppointment() }
        result_add_to_calendar_btn.setOnClickListener { presenter.onAddAppointmentToCalendar() }
    }

    override fun showCancelAppointmentDialog() {
        showConfirmDialog(
            R.string.appointment_title,
            R.string.cancel_appointment_message,
            onAccept = { presenter.onConfirmCancelAppointment() },
            onCancel = {})
    }

    override fun showAppointmentCanceledDialog() {
        showConfirmDialog(
            R.string.appointment_title,
            R.string.cancelled_appointment_message,
            onAccept = {})
    }

    override fun showErrorAppointmentCanceledDialog() {
        showErrorDialog(R.string.appointment_title, R.string.cancel_appointment_error_message)
    }

    override fun addAppointmentToCalendar(appointment: AppointmentEntity) {
        val intent = Utils.getAddToCalendarIntent(appointment)
        startActivity(intent)
    }

    override fun hideAppointment() {
        appointment_group.visibility = View.GONE
    }

    //endregion
}
