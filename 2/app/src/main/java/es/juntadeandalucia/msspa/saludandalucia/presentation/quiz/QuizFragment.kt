package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.quiz.adapter.QuizQuestionsAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.dialog.AppointmentDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_quiz.accept_btn
import kotlinx.android.synthetic.main.fragment_quiz.empty_group
import kotlinx.android.synthetic.main.fragment_quiz.not_available_gp
import kotlinx.android.synthetic.main.fragment_quiz.not_available_tv
import kotlinx.android.synthetic.main.fragment_quiz.questions_gp
import kotlinx.android.synthetic.main.fragment_quiz.questions_rv
import kotlinx.android.synthetic.main.fragment_quiz.root_sv
import kotlinx.android.synthetic.main.fragment_quiz.send_btn

class QuizFragment : BaseFragment(), QuizContract.View {

    companion object {
        private const val APPOINTMENT_DIALOG_TAG = "appointmentDialog"
    }

    private var appointmentDialog: AppointmentDialog? = null

    @Inject
    lateinit var presenter: QuizContract.Presenter

    private lateinit var onScrollListener: ViewTreeObserver.OnScrollChangedListener
    private lateinit var questionsAdapter: QuizQuestionsAdapter
    private var scrollPosition = 0

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_quiz

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
            onViewCreated()
            setupView(arguments?.getParcelable(Consts.ARG_USER)!!)
        }
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    //region - View methods
    override fun setupView() {
        onScrollListener = ViewTreeObserver.OnScrollChangedListener {
            scrollPosition = root_sv.scrollY
        }
        root_sv.viewTreeObserver.addOnScrollChangedListener(onScrollListener)
        accept_btn.setOnClickListener {
            presenter.onAcceptButtonClicked()
        }
        send_btn.setOnClickListener {
            presenter.onSendButtonClicked()
        }
        questionsAdapter =
            QuizQuestionsAdapter { questionEntity, response, shouldScroll, bottomPosition ->
                presenter.onResponseSelected(questionEntity, response)
                if (shouldScroll) {
                    root_sv.post(Runnable { root_sv.smoothScrollTo(0, bottomPosition) })
                }
            }
        questions_rv.adapter = questionsAdapter
    }

    override fun showQuizNotAvailable(nextTry: String) {
        not_available_tv.text = getString(R.string.quiz_not_available, nextTry)
        not_available_gp.visibility = View.VISIBLE
    }

    override fun startQuiz(questions: List<QuizQuestionEntity>) {
        questions_gp.visibility = View.VISIBLE
        questionsAdapter.submitList(questions)
        questionsAdapter.notifyDataSetChanged()
    }

    override fun enableSendButton() {
        send_btn.isEnabled = true
    }

    override fun navigateToResult(quizResultEntity: QuizResultEntity) {
        val bundle = bundleOf(Consts.ARG_QUIZ_RESULT to quizResultEntity)
        findNavController().navigate(R.id.action_quiz_dest_to_quiz_result_dest, bundle)
    }

    override fun showAppointment(appointment: AppointmentEntity) {
        appointmentDialog = AppointmentDialog(
            appointment,
            onCancelAppointment = { presenter.onCancelAppointment() },
            onAddToCalendar = { presenter.onAddAppointmentToCalendar() })
        activity?.supportFragmentManager?.let {
            appointmentDialog!!.show(
                it,
                APPOINTMENT_DIALOG_TAG
            )
        }
    }

    override fun dismissAppointmentDialog() {
        appointmentDialog?.dismiss()
        appointmentDialog = null
    }

    override fun removeScrollListener() {
        root_sv.viewTreeObserver.removeOnScrollChangedListener(onScrollListener)
    }

    override fun showConfirmDialog() {
        showDialog(
            title = R.string.quiz_confirm_dialog_title,
            message = R.string.quiz_confirm_dialog_message,
            positiveText = R.string.yes,
            onAccept = {
                presenter.sendQuizResponses()
            },
            cancelText = R.string.no,
            onCancel = {}
        )
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    override fun showErrorView() {
        empty_group.visibility = View.VISIBLE
        not_available_gp.visibility = View.GONE
        questions_gp.visibility = View.GONE
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
    //endregion
}
