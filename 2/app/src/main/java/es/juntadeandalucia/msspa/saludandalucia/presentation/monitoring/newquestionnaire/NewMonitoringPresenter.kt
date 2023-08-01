package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.NavBackPressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendMonitoringAnswersUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class NewMonitoringPresenter(
    private val sendNewMonitoringAnswersUseCase: SendMonitoringAnswersUseCase,
    private val backPressedBus: NavBackPressedBus
) :
    BasePresenter<NewMonitoringContract.View>(),
    NewMonitoringContract.Presenter {
    private lateinit var newMonitoringEntity: QuestionnaireEntity
    private lateinit var questController: QuestionnaireController
    private lateinit var questions: List<QuestionEntity>

    override fun onCreate(item: QuestionnaireEntity) {
        this.newMonitoringEntity = item
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.FOLLOWUP_CREATE_ACCESS

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
        setupView()
        backPressedBus.execute(onNext = {
            view.onBackPressed()
        }, onError = {
            Timber.e(it)
        })
    }

    private fun setupView() {
        with(view) {
            setupView(newMonitoringEntity)
            questions = newMonitoringEntity.questions
            questController = QuestionnaireController(newMonitoringEntity, view)
            questController.startQuiz()
            animateView()
            hideLoading()
        }
    }

    override fun onStop() {
        view.removeScrollListener()
    }

    override fun onSendButtonClicked() {
        if (questController.validateQuestionnaire()) {
            view.showConfirmDialog()
        }
    }

    override fun sendQuestionnaireAnswer() {
        view.showLoadingBlocking()
        sendNewMonitoringAnswersUseCase.params(
            responsesMap = questController.responsesMap,
            questionnaire = questController.questionnaire
        ).execute(onSuccess = { monitoring ->
            view.apply {
                hideLoading()
                showSendAnswersSuccess(
                    monitoring,
                    questController.questionnaire.title,
                    questController.questionnaire.id
                )
            }
        }, onError = {
            view.hideLoading()
            Timber.e(it)
            handleUnauthorizedException(
                exception = it,
                action = { view.showSendAnswersError() }
            )
        })
    }

    override fun unsubscribe() {
        sendNewMonitoringAnswersUseCase.clear()
    }
}
