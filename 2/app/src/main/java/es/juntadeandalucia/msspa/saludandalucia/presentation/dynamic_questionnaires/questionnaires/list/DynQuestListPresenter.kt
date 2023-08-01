package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.NoAuthorizedQuestionnaire
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetDynQuestListUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNewDynQuestUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class DynQuestListPresenter(
    private val getNewDynQuestUseCase: GetNewDynQuestUseCase,
    private val getDynQuestListUseCase: GetDynQuestListUseCase,
    private val sessionBus: SessionBus
) : BasePresenter<DynQuestListContract.View>(), DynQuestListContract.Presenter {

    private lateinit var quizId: String
    private lateinit var title: String
    private var questionnaireEntity: DynQuestionnaireEntity? = null
    private var questionnaireList: DynQuestListEntity = DynQuestListEntity(mutableListOf())
    private var shouldComeBack = false

    override fun onCreate(item: String, title: String) {
        this.quizId = item
        this.title = title
        view.setupList()
        if (!sessionBus.session.isUserAuthenticated()) {
            view.navigateToNewQuestionnaire(null, this.title, quizId)
        }
    }

    override fun getScreenNameTracking(): String? =
        Consts.Analytics.DYN_QUEST_QUIZ_DETAIL

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
        view.apply {
            animateView()
            setTitle(title)
        }

        if (sessionBus.session.isUserAuthenticated()) {
            if (questionnaireList.questsFilled.isNotEmpty()) {
                view.showList(questionnaireList = questionnaireList)
            } else {
                loadList()
            }
            showSubtitleAndButton()
        } else if (shouldComeBack) {
            view.comeBackToDinamic()
        }
    }

    private fun loadList() {
        view.showLoading()
        questionnaireList.questsFilled.clear()
        view.showList(questionnaireList)
        getDynQuestListUseCase.setId(quizId).execute(
            onNext = {
                with(questionnaireList.questsFilled) {
                    addAll(size, it.questsFilled)
                    view.apply {
                        hideLoading()
                        if (isNotEmpty()) {
                            showList(questionnaireList)
                        } else {
                            view.showNoQuestionnaire()
                        }
                    }
                }
            },
            onError = { error ->
                if (error !is NoAuthorizedQuestionnaire) {
                    view.showNoQuestionnaire()
                }
                handleError(error)
            },
            onCompleted = {}
        )
    }

    private fun showSubtitleAndButton() {
        questionnaireEntity?.apply {
            showButtonAndSubtitle(this)
        } ?: getNewQuestionnaire()
    }

    private fun showButtonAndSubtitle(dynQuestionnaireEntity: DynQuestionnaireEntity? = null) {
        with(view){
            showNewQuestionnaireButton(showError = dynQuestionnaireEntity == null)
            setSubtitle(dynQuestionnaireEntity?.purpose ?: "")
        }
    }

    private fun getNewQuestionnaire() {
        getNewDynQuestUseCase.setId(quizId).execute(
            onSuccess = {
                questionnaireEntity = it
                showSubtitleAndButton()
            },
            onError = { error ->
                if (error !is NoAuthorizedQuestionnaire) {
                    showButtonAndSubtitle()
                }
                handleError(error)
            }
        )
    }

    override fun onNewQuestionnaireClicked() {
        view.navigateToNewQuestionnaire(questionnaireEntity!!, title, quizId)
    }

    override fun onItemClicked(
        detailQuestionnaire: DynQuestListEntity.QuestFilledEntity,
        itemView: View
    ) {
        view.navigateToDetail(detailQuestionnaire, title, quizId)
    }

    override fun onRefresh() {
        loadList()
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
                }
            }
            is NoAuthorizedQuestionnaire -> {
                view.showQuestionnaireNotAuthorized()
            }
        }
    }

    override fun unsubscribe() {
        getNewDynQuestUseCase.clear()
        getDynQuestListUseCase.clear()
    }

    override fun onPause() {
        shouldComeBack = true
    }
}

