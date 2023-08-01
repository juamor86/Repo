package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.NavBackPressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNewDynQuestUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendDynQuestAnswersUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.PDF_TYPE
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import retrofit2.HttpException
import timber.log.Timber
import java.io.File
import java.net.HttpURLConnection

class DynQuestNewPresenter(
    private val sendDynQuestAnswersUseCase: SendDynQuestAnswersUseCase,
    private val getNewDynQuestUseCase: GetNewDynQuestUseCase,
    private val sessionBus: SessionBus,
    private val backPressedBus: NavBackPressedBus
) :
    BasePresenter<DynQuestNewContract.View>(),
    DynQuestNewContract.Presenter {
    private var newMonitoringEntity: DynQuestionnaireEntity? = null
    private var title: String? = null
    private lateinit var quizId: String
    private lateinit var quizIdHelp: String
    private lateinit var questController: DynQuestNewController
    private lateinit var questions: List<DynQuestionEntity>
    private var questionnaireEntityHelp: DynQuestionnaireEntity? = null

    private lateinit var attachmentQuest: DynQuestionEntity

    override fun onCreate(item: DynQuestionnaireEntity?, title: String?, quizId: String) {
        this.newMonitoringEntity = item
        this.title = title
        this.quizId = quizId
        this.quizIdHelp = quizId + "_HELP"
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.DYN_QUEST_CREATE

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
        setupView()
        showOrNotHelpButton()
        backPressedBus.execute(onNext = {
            view.onBackPressed()
        }, onError = {
            Timber.e(it)
        })
    }

    override fun onAttachmentClick(dynQuestionEntity: DynQuestionEntity) {
        attachmentQuest = dynQuestionEntity
        view.requestPermissions()
    }

    override fun attachmentCompleted(file: File) {
        val byteArray = if (file.name.substringAfterLast('.', "") != PDF_TYPE) {
            val image = Utils.rotateBitmapOrientation(file.absolutePath)
            Utils.compressPicture(image)
        } else {
            file.readBytes()
        }
        view.attachmentQuestionChanged()
        questController.onResponseSelected(
            attachmentQuest,
            DynQuestionEntity.ValueAttachment(
                title = file.name,
                data = Utils.encodeBase64(byteArray),
                size = file.length()
            ),
            0,
            false
        )
        view.showAttachmentResponse(file, questController.getPosition(attachmentQuest),byteArray.size)
    }

    override fun onHelpCLicked() {
        view.navigateToHelp(questionnaireEntityHelp!!)
    }

    private fun showOrNotHelpButton() {
        getNewDynQuestUseCase.setId(quizIdHelp).execute(
            onSuccess = {
                questionnaireEntityHelp = it
                questionnaireEntityHelp?.apply {
                    if (isQuestionnaireHelpValid(this)) {
                        view.showHelpButton()
                    }
                } ?: view.hidenHelpButton()
            },
            onError = { error ->
                when (error) {
                    is HttpException -> {
                        when (error.code()) {
                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                view.hidenHelpButton()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun isQuestionnaireHelpValid(questionnaireEntityHelp: DynQuestionnaireEntity): Boolean {
        var result = false
        questionnaireEntityHelp.questions.forEach { dynQuestionEntity ->
            val dynQuestionHelp = dynQuestionEntity as DynQuestionEntity.NotSupportedQuestionEntity
            if (!dynQuestionHelp.valueCodingExtension.isNullOrEmpty()) {
                result = true
            }
        }
        return result
    }

    private fun setupView() {
        view.showLoading()
        newMonitoringEntity?.let { newMonitoringEntity ->
            loadForm(newMonitoringEntity)
        } ?: getNewDynQuestUseCase.setId(quizId).execute(onSuccess = {
            this.newMonitoringEntity = it
            if (title == null) {
                title = it.name
            }
            loadForm(it)
        }, onError = {
            view.hideLoading()
            view.showServiceNotAvailable()
            Timber.e(it)
        })
    }

    private fun loadForm(newMonitoringEntity: DynQuestionnaireEntity) {
        with(view) {
            setupView(newMonitoringEntity, title!!)
            questions = newMonitoringEntity.questions
            questController = DynQuestNewController(newMonitoringEntity, view)
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
            if (questController.areFilesAreTooLarge()) {
                view.showFilesAreTooLarge()
            } else {
                if (sessionBus.session.isUserAuthenticated()) {
                    view.showConfirmDialog()
                } else {
                    view.showConFirmDialogLoggedout()
                }
            }
        }
    }

    override fun sendQuestionnaireAnswer() {
        view.showLoadingBlocking()
        sendDynQuestAnswersUseCase.params(
            responsesMap = questController.responsesMap,
            questionnaire = questController.questionnaire
        ).execute(onSuccess = { monitoring ->
            view.apply {
                sendEvent(Consts.Analytics.DYN_QUEST_SEND_SUCCESS)
                hideLoading()
                showSendAnswersSuccess(
                    monitoring,
                    title!!,
                    questController.questionnaire.id
                )
            }
        }, onError = { error ->
            view.sendEvent(Consts.Analytics.DYN_QUEST_SEND_FAILURE)
            handleError(error)
        })
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        view.hideLoading()
        showOnError(error)
    }

    private fun showOnError(error: Throwable) {
        when (error) {
            is HttpException -> {
                when (error.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN, HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        sessionBus.onUnauthorizedEvent()
                    }
                    else -> view.showSendAnswersError()
                }
            }
            else -> {
                view.showSendAnswersError()
            }
        }
    }

    override fun unsubscribe() {
        sendDynQuestAnswersUseCase.clear()
    }
}
