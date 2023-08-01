package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire.adapter.QuestionnaireAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Tracker
import kotlinx.android.synthetic.main.fragment_monitoring_new.*
import kotlinx.android.synthetic.main.view_item_question.view.*
import javax.inject.Inject

class NewMonitoringFragment : BaseFragment(), NewMonitoringContract.View {

    @Inject
    lateinit var presenter: NewMonitoringContract.Presenter

    private lateinit var onScrollListener: ViewTreeObserver.OnScrollChangedListener
    private lateinit var questionsAdapter: QuestionnaireAdapter
    private var scrollPosition = 0
    private var formChanged = false

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun bindLayout(): Int = R.layout.fragment_monitoring_new

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = arguments?.get(Consts.ARG_ITEM) as QuestionnaireEntity
        presenter.onCreate(item)
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    //region - View methods
    override fun setupView(questionnaire: QuestionnaireEntity) {
        title_section_tv.text = questionnaire.name
        onScrollListener = ViewTreeObserver.OnScrollChangedListener {
            if (content_ns != null) {
                scrollPosition = content_ns.scrollY
            }
        }
        content_ns.viewTreeObserver.addOnScrollChangedListener(onScrollListener)
        send_btn.setOnClickListener {
            questions_rv.clearFocus()
            presenter.onSendButtonClicked()
        }
    }

    override fun enableSendButton() {
        send_btn.isEnabled = true
    }

    override fun disableSendButton() {
        send_btn.isEnabled = false
    }

    override fun removeScrollListener() {
        content_ns.viewTreeObserver.removeOnScrollChangedListener(onScrollListener)
    }

    override fun showConfirmDialog() {
        showDialog(
            title = R.string.quiz_confirm_dialog_title,
            message = R.string.quiz_confirm_dialog_message,
            positiveText = R.string.accept,
            onAccept = {
                presenter.sendQuestionnaireAnswer()
            },
            cancelText = R.string.cancel,
            onCancel = {}
        )
    }

    override fun setupQuestionnaire(
        questionnaire: QuestionnaireEntity,
        questionsList: MutableList<QuestionEntity>,
        questionnaireHelper: QuestionnaireController
    ) {
        questionsAdapter =
            QuestionnaireAdapter(questionnaireHelper,::onQuestionModified)
        questions_rv.adapter = questionsAdapter
        questionsAdapter.submitList(questionsList)
        questionsAdapter.notifyDataSetChanged()
    }

    private fun onQuestionModified(){
        formChanged = true
    }

    override fun scrollTo(scrollPosition: Int) {
        content_ns?.postDelayed({ content_ns?.smoothScrollTo(0, scrollPosition) }, 300)
    }

    override fun focusElement(position: Int) {
        questions_rv?.postDelayed({
            questions_rv?.findViewHolderForAdapterPosition(position)?.itemView?.requestFocus()
        }, 300)
    }

    override fun startQuiz() {
        questions_rv.visibility = View.VISIBLE
    }

    override fun setQuestionOk(position: Int) {
        questions_rv.findViewHolderForAdapterPosition(position)?.itemView?.question_tv?.setTextColor(
            ContextCompat.getColor(requireContext(),R.color.green_dark))
    }


    override fun setQuestionError(position: Int) {
        questions_rv.findViewHolderForAdapterPosition(position)?.itemView?.question_tv?.setTextColor(
            ContextCompat.getColor(requireContext(),R.color.red_orange))
    }

    override fun onBackPressed() {
        if (formChanged) {
            showBackPressAlertDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showBackPressAlertDialog() {
        showConfirmDialog(title = R.string.back_pressed_dyn_new_form_dialog_text, onAccept = {
            findNavController().popBackStack()
        }, onCancel = { })
    }

    override fun showQuestion(position: Int) {
        questionsAdapter.notifyItemInserted(position)
    }

    override fun hideQuestion(position: Int) {
        if (position > -1) {
            questionsAdapter.notifyItemRemoved(position)
        }
    }

    override fun informQuestionResponseNotValid() {
        var isShown = false
        if (!isShown) {
            isShown = true
            showConfirmDialog(R.string.question_response_not_valid, onAccept = {
                isShown = false
            })
        }
    }

    override fun informQuestionResponseLessMin() {
        showConfirmDialog(R.string.question_response_less_min)
    }

    override fun informQuestionResponseMoreMax() {
        showConfirmDialog(R.string.question_response_more_max)
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    override fun showSendAnswersSuccess(
        detailProgram: MonitoringListEntity.QuestFilledEntity,
        title: String,
        id: String
    ) {
        sendEvent(Consts.Analytics.FOLLOWUP_CREATED_SUCESS)
        showSuccessDialog(
            title = R.string.monitoring_send_success,
            onAccept = {
                val bundle =
                    bundleOf(Consts.ARG_QUEST_FILLED to detailProgram, Consts.ARG_TITLE to title, Consts.ARG_ID to id)
                findNavController().navigate(R.id.action_new_program_dest_to_detail_dest, bundle)
            })
    }

    override fun showSendAnswersError() {
        sendEvent(Consts.Analytics.FOLLOWUP_CREATED_FAILURE)
        showErrorDialog(R.string.monitoring_send_error)
    }

    override fun animateView() {
        with(content_cl) {
            visibility = View.VISIBLE
            animateViews(
                this,
                animId = R.anim.slide_from_bottom
            )
        }
    }
}
