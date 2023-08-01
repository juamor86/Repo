package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.TWO_MB_SIZE

class DynQuestNewController(
    val questionnaire: DynQuestionnaireEntity,
    private val view: DynQuestNewContract.View
) {
    private val actionsMap = mutableMapOf<String, MutableList<DynQuestionEntity>>()
    var responsesMap = mutableMapOf<DynQuestionEntity, MutableList<Any>>()

    private var questions: List<DynQuestionEntity> = questionnaire.questions
    private var questionsView: MutableList<DynQuestionEntity>

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
        question: DynQuestionEntity,
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

            if(index > -1){
                if (multipleSelection) {
                    responsesMap[question] = response as MutableList<Any>
                } else {
                    if (question is DynQuestionEntity.AttachmentQuestionEntity) {
                        if ((response as DynQuestionEntity.ValueAttachment).data.isEmpty()) {
                            responsesMap.remove(question)
                        } else {
                            responsesMap[question] = mutableListOf(response)
                        }
                    } else {
                        responsesMap[question] = mutableListOf(response)
                    }
                }
            }
            view.scrollTo(scrollPosition )
        } else {
            question.error = true
            view.setQuestionError(index)
            responsesMap.remove(question)
            informError(response, question, scrollPosition)
        }
        validateQuestionnaire()
    }

    private fun informError(response: Any, question: DynQuestionEntity, scrollPosition: Int) {
        if (questionsView.indexOf(question) != -1) {
                when (question) {
                    is DynQuestionEntity.IntegerQuestionEntity -> {
                        if (response.toString().toInt() < question.minValue!!) {
                            view.informQuestionResponseLessMin()
                        } else {
                            view.informQuestionResponseMoreMax()
                        }
                    }
                    is DynQuestionEntity.DecimalQuestionEntity -> {
                        if (response.toString().toFloat() < question.minValue!!) {
                            view.informQuestionResponseLessMin()
                        } else {
                            view.informQuestionResponseMoreMax()
                        }
                    }
                    is DynQuestionEntity.QuantityQuestionEntity -> {
                        if (response.toString().toInt() < question.minValue!!) {
                            view.informQuestionResponseLessMin()
                        } else {
                            view.informQuestionResponseMoreMax()
                        }
                    }
                    is DynQuestionEntity.ChoicesQuestionEntity -> {
                        if ((response as ArrayList<*>).size > question.valueRange!!.high) {
                            view.informQuestionChoiceResponseMoreMax()
                        }
                    }
                    is DynQuestionEntity.OpenChoicesQuestionEntity -> {
                        if ((response as ArrayList<*>).size > question.valueRange!!.high) {
                            view.informQuestionChoiceResponseMoreMax()
                        }
                    }
                    is DynQuestionEntity.UrlQuestionEntity -> {
                        view.informQuestionUrlFormatNotValid(scrollPosition)
                    }
                    else -> {
                        view.informQuestionResponseNotValid(scrollPosition)
                    }
                }
        }
    }

    private fun validateResponse(question: DynQuestionEntity, response: Any): Boolean {
        return question.checkResponse(response)
    }

    private fun checkAction(questionSource: DynQuestionEntity, response: Any) {
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
        questionSource: DynQuestionEntity,
        questionTarget: DynQuestionEntity,
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

    private fun hideQuestion(questionTarget: DynQuestionEntity) {
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

    fun getPosition(questionTarget: DynQuestionEntity) = questionsView.indexOf(questionTarget)

    private fun showQuestion(questionTarget: DynQuestionEntity, position: Int) {
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

    fun areFilesAreTooLarge(): Boolean {
        var totalFileSize = 0L
        responsesMap.forEach { response ->
            if (response.key is DynQuestionEntity.AttachmentQuestionEntity) {
                totalFileSize += (response.value[0] as DynQuestionEntity.ValueAttachment).size
            }
        }
        return totalFileSize > TWO_MB_SIZE
    }
}
