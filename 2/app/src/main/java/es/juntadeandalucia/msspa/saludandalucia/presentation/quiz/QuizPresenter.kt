package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz

import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.CancelAppointmentUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetQuizQuestionsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetQuizSessionUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendQuizResponsesUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class QuizPresenter(
    private val getQuizQuestionsUseCase: GetQuizQuestionsUseCase,
    private val sendQuizResponsesUseCase: SendQuizResponsesUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val getQuizSession: GetQuizSessionUseCase
) :
    BasePresenter<QuizContract.View>(), QuizContract.Presenter {

    private lateinit var appointment: AppointmentEntity
    private lateinit var questions: List<QuizQuestionEntity>
    private var responsesMap = mutableMapOf<String, String>()
    private lateinit var user: QuizUserEntity

    override fun setupView(user: QuizUserEntity) {
        view.setupView()
        this.user = user
        loadQuizQuestions()
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.QUIZ_COVID_TRIAGE_SCREEN_ACCESS

    private fun loadQuizQuestions() {
        view.showLoading()
        getQuizSession.execute()?.let { quizSession ->
            if(quizSession.authorizationToken.isNullOrEmpty()){
                view.apply {
                    hideLoading()
                    showErrorView()
                }
                return
            }
            getQuizQuestionsUseCase.params(quizSession).execute(
                onSuccess = { quizEntity ->
                    setupView(quizEntity)
                },
                onError = {
                    Timber.e("Error getting the quiz questions")
                    view.apply {
                        hideLoading()
                        showErrorView()
                        when (it) {
                            is TooManyRequestException -> showTooManyRequestDialog()
                            else -> showErrorDialog()
                        }
                    }
                }
            )
        }




    }

    private fun setupView(quizEntity: QuizEntity) {
        view.apply {
            hideLoading()
            quizEntity.appointment?.let {
                appointment = it
                view.showAppointment(it)
            }

            run {
                if (quizEntity.available) {
                    questions = quizEntity.questions
                    startQuiz(quizEntity.questions)
                } else {
                    showQuizNotAvailable(quizEntity.nextTry)
                }
            }
        }
    }

    override fun onStop() {
        view.removeScrollListener()
    }

    override fun onResponseSelected(questionEntity: QuizQuestionEntity, response: String) {
        validateQuiz()
        responsesMap[questionEntity.questionId] = response
    }

    private fun validateQuiz() {
        if (responsesMap.size == questions.size - 1) {
            view.enableSendButton()
        }
    }

    override fun onAcceptButtonClicked() {
        view.closeView()
    }

    override fun onSendButtonClicked() {
        view.showConfirmDialog()
    }

    override fun sendQuizResponses() {
        view.showLoading()
        getQuizSession.execute()?.let { quizSession ->
            if(quizSession.authorizationToken.isNullOrEmpty()){
                view.hideLoading()
                return
            }
            sendQuizResponsesUseCase.params(user, responsesMap.toMap(), quizSession).execute(
                onSuccess = {
                    view.apply {
                        hideLoading()
                        navigateToResult(it)
                    }
                },
                onError = {
                    Timber.e("Error sending the quiz responses")
                    view.apply {
                        hideLoading()
                        when (it) {
                            is TooManyRequestException -> showTooManyRequestDialog()
                            else -> showErrorDialog()
                        }
                    }
                }
            )
        }
    }

    override fun closeQuiz() {
        view.closeView()
    }

    override fun onAddAppointmentToCalendar() {
        view.addAppointmentToCalendar(appointment)
    }

    override fun onCancelAppointment() {
        view.showCancelAppointmentDialog()
    }

    override fun onConfirmCancelAppointment() {
        cancelAppointmentUseCase.execute(
            onComplete = {
                view.showAppointmentCanceledDialog()
                view.dismissAppointmentDialog()
            },
            onError = {
                Timber.e(it)
                view.showErrorAppointmentCanceledDialog()
            })
    }

    override fun unsubscribe() {
        getQuizQuestionsUseCase.clear()
        sendQuizResponsesUseCase.clear()
    }
}
