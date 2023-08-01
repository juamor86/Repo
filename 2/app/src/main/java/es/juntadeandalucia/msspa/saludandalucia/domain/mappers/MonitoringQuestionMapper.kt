package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.EnableWhenData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ItemData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ValueCodingData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Resource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat

class MonitoringQuestionMapper {

    companion object {
        fun convert(monitoringProgram: MonitoringListData): MonitoringListEntity =
            with(monitoringProgram) {
                MonitoringListEntity(
                    questsFilled = entry[0].resource.map { convert(it) }.toMutableList()
                )
            }

        fun convert(it: Resource): MonitoringListEntity.QuestFilledEntity {
            with(it) {
                val questions = mutableListOf<QuestionEntity>()

                for (item in item) {
                    val question = convert(item)
                    questions.add(question)
                    if (item.item.isNotEmpty()) {
                        questions.addAll(item.item.map { convert(it, question) })
                    }
                }

                return MonitoringListEntity.QuestFilledEntity(
                    date = this.meta.lastUpdated,
                    questions = questions,
                    description = getDescription(extension)
                )
            }
        }

        private fun getDescription(extension: List<ExtensionData>): String {
            for (ext in extension) {
                if (ext.url == "sugerencias") {
                    return ext.valueString
                }
            }
            return ""
        }

        fun convert(it: ItemData, group: QuestionEntity? = null): QuestionEntity {
            with(it) {
                val answerAux = if (!answer.isNullOrEmpty()) convert(answer[0]) else null
                val question =
                    when (getType(this)) {
                        QuestionEntity.QuestionType.TEXT.nameStr -> QuestionEntity.TextQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.BOOLEAN.nameStr -> QuestionEntity.BooleanQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.DATE.nameStr -> QuestionEntity.DateQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.DATETIME.nameStr -> QuestionEntity.DateTimeQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.DECIMAL.nameStr -> QuestionEntity.DecimalQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.INTEGER.nameStr -> QuestionEntity.IntegerQuestionEntity(
                            questionId = linkId,
                            question = text,
                            minValue = getMinValue(extension),
                            maxValue = getMaxValue(extension),
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.TIME.nameStr -> QuestionEntity.TimeQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.CHOICE.nameStr -> QuestionEntity.ChoicesQuestionEntity(
                            questionId = linkId,
                            question = text,
                            options = getOptions(this),
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.OPEN_CHOICE.nameStr -> QuestionEntity.OpenChoicesQuestionEntity(
                            questionId = linkId,
                            question = text,
                            options = getOptions(this),
                            mandatory = required,
                            answer = if (!answer.isNullOrEmpty()) QuestionEntity.AnswerOptionEntity() else null,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.QUANTITY.nameStr -> QuestionEntity.QuantityQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        QuestionEntity.QuestionType.GROUP.nameStr -> QuestionEntity.GroupQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        else -> QuestionEntity.NotSupportedQuestionEntity(
                            linkId,
                            text,
                            mandatory = required
                        )
                    }

                question.group = group
                return question
            }
        }

        private fun getMinValue(extension: List<ExtensionData>?): Int? {
            if (extension != null) {
                for (ext in extension) {
                    if (ext.url == "minValue") {
                        return ext.valueInteger
                    }
                }
            }
            return null
        }

        private fun getMaxValue(extension: List<ExtensionData>?): Int? {
            if (extension != null) {
                for (ext in extension) {
                    if (ext.url == "maxValue") {
                        return ext.valueInteger
                    }
                }
            }
            return null
        }

        private fun getType(itemData: ItemData): String {
            if (!itemData.type.isNullOrEmpty()) {
                return itemData.type
            } else {
                itemData.extension?.forEach { extension ->
                    if (extension.url == "type") {
                        return extension.valueCode
                    }
                }
            }
            return ""
        }

        private fun getOptions(itemData: ItemData): List<QuestionEntity.AnswerOptionEntity> {
            return if (itemData.answerOption?.isEmpty()!!) {
                convertFilledChoice(itemData.answer!!)
            } else {
                val result = mutableListOf<QuestionEntity.AnswerOptionEntity>()
                for (answer in itemData.answerOption) {
                    result.add(convert(answer))
                }
                result
            }
        }

        private fun convertFilledChoice(answerList: List<AnswerData>): List<QuestionEntity.AnswerOptionEntity> {
            val result = mutableListOf<QuestionEntity.AnswerOptionEntity>()
            for (answer in answerList) {
                val item = convert(answer)
                if (answer.extension.isNotEmpty() && answer.extension[0].valueBoolean) {
                    item.selected = true
                }
                result.add(item)
            }
            return result
        }

        private fun convert(answer: AnswerData): QuestionEntity.AnswerOptionEntity {
            with(answer) {
                return QuestionEntity.AnswerOptionEntity(
                    valueDate = UtilDateFormat.formatDateString(this.valueDate),
                    valueTime = UtilDateFormat.formatTimeString(this.valueTime),
                    valueDateTime = UtilDateFormat.formatDateTime(this.valueDateTime),
                    valueDecimal = this.valueDecimal,
                    valueString = this.valueString,
                    valueBoolean = this.valueBoolean,
                    valueInteger = this.valueInteger,
                    valueCoding = this.valueCoding?.let { convert(it) }
                )
            }
        }

        private fun convertEnableWhenList(enableWhenDataList: List<EnableWhenData>): List<QuestionEntity.EnableWhen?> =
            enableWhenDataList.map { convertEnableWhen(it) }

        private fun convertEnableWhen(it: EnableWhenData): QuestionEntity.EnableWhen? {
            with(it) {
                answerBoolean?.run {
                    return QuestionEntity.EnableWhenBoolean(
                        operator,
                        question,
                        this
                    )
                }
                answerInteger?.run {
                    return QuestionEntity.EnableWhenInteger(
                        operator,
                        question,
                        this
                    )
                }
                answerCoding?.run {
                    return QuestionEntity.EnableWhenOpenChoice(
                        operator,
                        question,
                        convert(this)
                    )
                }
                answerQuantity?.run {
                    return QuestionEntity.EnableWhenFloat(
                        operator,
                        question,
                        this.value
                    )
                }

                return null
            }
        }

        fun convert(valueCodingData: ValueCodingData) = with(valueCodingData) {
            QuestionEntity.ValueCodingEntity(
                code,
                display,
                version = version,
                system = system
            )
        }

        fun convert(it: SendNewMonitoringAnswerData): MonitoringListEntity.QuestFilledEntity {
            with(it) {
                val questions = mutableListOf<QuestionEntity>()

                for (item in item) {
                    val question = convert(item)
                    questions.add(question)
                    if (item.item.isNotEmpty()) {
                        questions.addAll(item.item.map { convert(it, question) })
                    }
                }

                return MonitoringListEntity.QuestFilledEntity(
                    date = this.authored,
                    questions = questions,
                    description = getDescription(extension)
                )
            }
        }

        fun convert(it: SendMonitoringAnswersResponseData): MonitoringListEntity.QuestFilledEntity {
            with(it) {
                val questions = mutableListOf<QuestionEntity>()

                for (item in item) {
                    val question = convert(item)
                    questions.add(question)
                    if (item.item.isNotEmpty()) {
                        questions.addAll(item.item.map { convert(it, question) })
                    }
                }

                return MonitoringListEntity.QuestFilledEntity(
                    date = this.authored,
                    questions = questions,
                    description = getDescription(extension)
                )
            }
        }

        fun convertToItemData(
            questionEntity: QuestionEntity,
            value: List<Any>
        ): ItemData {
            return ItemData(
                linkId = questionEntity.questionId,
                extension = null,
                enableWhen = null,
                answerOption = null,
                text = questionEntity.question,
                type = getQuestionType(questionEntity),
                answer = value.map { any -> convertAnyToAnswer(questionEntity, any) })
        }

        fun convertToGroupItemData(
            group: QuestionEntity
        ): ItemData {
            return ItemData(
                text = group.question,
                linkId = group.questionId,
                extension = null,
                enableWhen = null,
                answerOption = null,
                answer = null
            )
        }

        private fun convertAnyToAnswer(questionEntity: QuestionEntity, any: Any) =
            when (questionEntity) {
                is QuestionEntity.TextQuestionEntity -> AnswerData(valueString = any.toString())
                is QuestionEntity.BooleanQuestionEntity -> AnswerData(valueBoolean = any as Boolean)
                is QuestionEntity.IntegerQuestionEntity -> AnswerData(valueInteger = any.toString())
                is QuestionEntity.DecimalQuestionEntity -> AnswerData(valueDecimal = any.toString())
                is QuestionEntity.DateQuestionEntity -> AnswerData(valueDate = any.toString())
                is QuestionEntity.DateTimeQuestionEntity -> AnswerData(valueDateTime = any.toString())
                is QuestionEntity.TimeQuestionEntity -> AnswerData(valueTime = any.toString())
                is QuestionEntity.ChoicesQuestionEntity -> AnswerData(valueString = any.toString())
                is QuestionEntity.OpenChoicesQuestionEntity -> AnswerData(valueString = any.toString())
                is QuestionEntity.QuantityQuestionEntity -> AnswerData(valueString = any.toString())
                else -> AnswerData(valueString = any.toString())
            }

        private fun getQuestionType(questionEntity: QuestionEntity): String =
            when (questionEntity) {
                is QuestionEntity.TextQuestionEntity -> QuestionEntity.QuestionType.TEXT.nameStr
                is QuestionEntity.BooleanQuestionEntity -> QuestionEntity.QuestionType.BOOLEAN.nameStr
                is QuestionEntity.IntegerQuestionEntity -> QuestionEntity.QuestionType.INTEGER.nameStr
                is QuestionEntity.DecimalQuestionEntity -> QuestionEntity.QuestionType.DECIMAL.nameStr
                is QuestionEntity.DateQuestionEntity -> QuestionEntity.QuestionType.DATE.nameStr
                is QuestionEntity.DateTimeQuestionEntity -> QuestionEntity.QuestionType.DATETIME.nameStr
                is QuestionEntity.TimeQuestionEntity -> QuestionEntity.QuestionType.TIME.nameStr
                is QuestionEntity.ChoicesQuestionEntity -> QuestionEntity.QuestionType.CHOICE.nameStr
                is QuestionEntity.OpenChoicesQuestionEntity -> QuestionEntity.QuestionType.OPEN_CHOICE.nameStr
                is QuestionEntity.QuantityQuestionEntity -> QuestionEntity.QuestionType.QUANTITY.nameStr
                else -> QuestionEntity.QuestionType.NOT_SUPPORTED.nameStr
            }

    }
}
