package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class QuestionnaireController(
    val questionnaire: QuestionnaireEntity,
    private val view: NewMonitoringContract.View
) {
    private val actionsMap = mutableMapOf<String, MutableList<QuestionEntity>>()
    var responsesMap = mutableMapOf<QuestionEntity, MutableList<Any>>()

    private var questions: List<QuestionEntity> = questionnaire.questions
    private var questionsView: MutableList<QuestionEntity>

    init {
        questionsView = questions.toMutableList()
        createActionsMap()
        filterNonVisibleQuestions()
        view.setupQuestionnaire(questionnaire, questionsView, this)
    }

    private fun createActionsMap() {
        for (question in questions) {
            if (question.enableWhen != null) {
                val key = question.enableWhen!!.question
                if (actionsMap.containsKey(key)) {
                    actionsMap[key]!!.add(question)
                } else {
                    actionsMap[key] = mutableListOf(question)
                }
            }
        }
    }

    private fun filterNonVisibleQuestions() {
        questionsView =
            questionsView.filter { question -> question.enableWhen == null }.toMutableList()
    }

    fun onResponseSelected(
        question: QuestionEntity,
        response: Any,
        scrollPosition: Int,
        multipleSelection: Boolean
    ) {
        val index = questionsView.indexOf(question)

        if (response.toString().trim().isEmpty()) {
            view.scrollTo(scrollPosition)
            responsesMap.remove(question)
            return
        }

        if (validateResponse(question, response)) {
            question.error = false
            view.setQuestionOk(index)
            checkAction(question, response)
            if (multipleSelection) {
                responsesMap[question]?.add(response) ?: let {
                    responsesMap.put(question, mutableListOf(response))
                }
            } else {
                responsesMap[question] = mutableListOf(response)
            }
            if (question is QuestionEntity.BooleanQuestionEntity) {
                view.focusElement(index + 1)
                view.scrollTo(scrollPosition + Consts.PLUS_SCROLL_POSITION)
            }
            view.scrollTo(scrollPosition )
        } else {
            question.error = true
            view.setQuestionError(index)
            responsesMap.remove(question)
            informError(response, question)
        }
        validateQuestionnaire()
    }

    private fun informError(response: Any, question: QuestionEntity) {

        // TODO This should be improved. Temporary fix
        if (response.toString().isNotEmpty()) {
            when (question) {
                is QuestionEntity.IntegerQuestionEntity -> {
                    if (response.toString().toInt() < question.minValue!!) {
                        view.informQuestionResponseLessMin()
                    } else {
                        view.informQuestionResponseMoreMax()
                    }
                }
                is QuestionEntity.DecimalQuestionEntity -> {
                    if (response.toString().toFloat() < question.minValue!!) {
                        view.informQuestionResponseLessMin()
                    } else {
                        view.informQuestionResponseMoreMax()
                    }
                }
                is QuestionEntity.QuantityQuestionEntity -> {
                    if (response.toString().toInt() < question.minValue!!) {
                        view.informQuestionResponseLessMin()
                    } else {
                        view.informQuestionResponseMoreMax()
                    }
                }
                else -> {
                    view.informQuestionResponseNotValid()
                }
            }
        } else {
            view.informQuestionResponseNotValid()
        }


    }


    private fun validateResponse(question: QuestionEntity, response: Any): Boolean {
        return question.checkResponse(response)
    }

    private fun checkAction(questionSource: QuestionEntity, response: Any) {
        actionsMap[questionSource.questionId]?.apply {
            for (questionTarget in reversed()) {
                evaluateAction(
                    questionSource,
                    questionTarget,
                    response
                )
            }
        }
    }

    private fun evaluateAction(
        questionSource: QuestionEntity,
        questionTarget: QuestionEntity,
        response: Any
    ) {
        val enabled = questionTarget.enableWhen?.isEnabled(response)
        val position = questionsView.indexOf(questionTarget.group ?: questionSource) + 1
        enabled?.run {
            if (this) {
                showQuestion(questionTarget, position)
            } else {
                hideQuestion(questionTarget)
            }
        }
    }

    private fun hideQuestion(questionTarget: QuestionEntity) {
        val position = questionsView.indexOf(questionTarget)
        if (position > -1) {
            responsesMap.remove(questionTarget)
            questionsView.removeAt(position)
            view.hideQuestion(position)

            actionsMap[questionTarget.questionId]?.apply {
                for (questionTarget in this) {
                    hideQuestion(questionTarget)
                }
            }
        }
    }

    private fun showQuestion(questionTarget: QuestionEntity, position: Int) {
        val index = questionsView.indexOf(questionTarget)
        if (index == -1) {
            questionsView.add(position, questionTarget)
            view.showQuestion(position)
        }
    }

    fun validateQuestionnaire(): Boolean {
        var isValid = true
        for (question in questions) {
            if (question.mandatory && (responsesMap[question] == null && questionsView.contains(
                    question
                ))
            ) {
                isValid = false
                break
            }
        }
        for (question in questionsView) {
            if (question.error) {
                isValid = false
                break
            }
        }
        if (isValid) {
            view.enableSendButton()
        } else {
            view.disableSendButton()
        }
        return isValid
    }

    fun startQuiz() {
        view.disableSendButton()
        view.startQuiz()
    }
}
