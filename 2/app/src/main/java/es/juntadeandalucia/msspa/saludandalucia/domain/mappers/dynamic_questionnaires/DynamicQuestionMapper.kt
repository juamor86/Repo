package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.dynamic_questionnaires

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.ValueAttachment
import es.juntadeandalucia.msspa.saludandalucia.data.entities.ValueQuantity

import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.Resource
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendNewDynQuestAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.*
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.RESPONSE_NUM_CHOICE_PARAM
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat

class DynamicQuestionMapper {

    companion object {
        fun convert(monitoringProgram: DynQuestListData): DynQuestListEntity =
            with(monitoringProgram) {
                DynQuestListEntity(
                    questsFilled = entry[0].resource.map { convert(it) }.toMutableList()
                )
            }

        fun convert(it: Resource): DynQuestListEntity.QuestFilledEntity {
            with(it) {
                val questions = mutableListOf<DynQuestionEntity>()

                for (item in item) {
                    val question = convert(item)
                    questions.add(question)
                    if (item.item.isNotEmpty()) {
                        questions.addAll(item.item.map { convert(it, question) })
                    }
                }

                return DynQuestListEntity.QuestFilledEntity(
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

        fun convert(it: ItemData, group: DynQuestionEntity? = null): DynQuestionEntity {
            with(it) {
                val answerAux = if (!answer.isNullOrEmpty()) convert(answer[0]) else null
                val question =
                    when (getType(this)) {
                        DynQuestionEntity.QuestionType.TEXT.nameStr -> DynQuestionEntity.TextQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.URL.nameStr -> DynQuestionEntity.UrlQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.BOOLEAN.nameStr -> DynQuestionEntity.BooleanQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.DATE.nameStr -> DynQuestionEntity.DateQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.DATETIME.nameStr -> DynQuestionEntity.DateTimeQuestionEntity(
                            questionId = this.linkId,
                            question = this.text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.DECIMAL.nameStr -> DynQuestionEntity.DecimalQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.INTEGER.nameStr -> DynQuestionEntity.IntegerQuestionEntity(
                            questionId = linkId,
                            question = text,
                            minValue = getMinValue(extension),
                            maxValue = getMaxValue(extension),
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.TIME.nameStr -> DynQuestionEntity.TimeQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.CHOICE.nameStr -> DynQuestionEntity.ChoicesQuestionEntity(
                            questionId = linkId,
                            question = text,
                            options = getOptions(this),
                            valueRange = getValueRange(extension),
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.OPEN_CHOICE.nameStr -> DynQuestionEntity.OpenChoicesQuestionEntity(
                            questionId = linkId,
                            question = text,
                            options = getOptions(this),
                            valueRange = getValueRange(extension),
                            mandatory = required,
                            answer = if (!answer.isNullOrEmpty()) DynQuestionEntity.AnswerOptionEntity() else null,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.QUANTITY.nameStr -> DynQuestionEntity.QuantityQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            minValue = getMinValue(extension),
                            maxValue = getMaxValue(extension),
                            unit = getUnit(extension),
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.ATTACHMENT.nameStr -> DynQuestionEntity.AttachmentQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        DynQuestionEntity.QuestionType.GROUP.nameStr -> DynQuestionEntity.GroupQuestionEntity(
                            questionId = linkId,
                            question = text,
                            mandatory = required,
                            answer = answerAux,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() }
                        )
                        else -> DynQuestionEntity.NotSupportedQuestionEntity(
                            linkId,
                            text,
                            mandatory = required,
                            enableWhen = enableWhen?.let { it -> convertEnableWhenList(it).firstOrNull() },
                            valueCodingExtension = convertValueCoding(extension)
                        )
                    }

                question.group = group
                return question
            }
        }

        private fun getValueRange(extensionList: List<ExtensionData>?): DynQuestionEntity.ValueRangeEntity? {
            if (extensionList != null) {
                for (ext in extensionList) {
                    if (ext.url == RESPONSE_NUM_CHOICE_PARAM) {
                        return ext.valueRange?.let { convert(it) }
                    }
                }
            }
            return null
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

        private fun getUnit(extension: List<ExtensionData>?): String{
            if (extension != null) {
                for (ext in extension) {
                    if (ext.url == Consts.UNIT_PARAM) {
                        return ext.valueCode
                    }
                }
            }
            return ""
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

        private fun getOptions(itemData: ItemData): List<DynQuestionEntity.AnswerOptionEntity> {
            return if (itemData.answerOption == null || itemData.answerOption.isEmpty()) {
                convertFilledChoice(itemData.answer!!)
            } else {
                val result = mutableListOf<DynQuestionEntity.AnswerOptionEntity>()
                for (answer in itemData.answerOption) {
                    result.add(convert(answer))
                }
                result
            }
        }

        private fun convertFilledChoice(answerList: List<AnswerData>): List<DynQuestionEntity.AnswerOptionEntity> {
            val result = mutableListOf<DynQuestionEntity.AnswerOptionEntity>()
            for (answer in answerList) {
                val item = convert(answer)
                if (answer.extension.isNotEmpty() && answer.extension[0].valueBoolean) {
                    item.selected = true
                } else if (answer.extension.isEmpty()){
                    item.isOpenChiceText = true
                }
                result.add(item)
            }
            return result
        }

        private fun convert(answer: AnswerData): DynQuestionEntity.AnswerOptionEntity {
            with(answer) {
                return DynQuestionEntity.AnswerOptionEntity(
                    valueDate = this.valueDate,
                    valueTime = this.valueTime,
                    valueUri = this.valueUri,
                    valueDateTime = this.valueDateTime,
                    valueDecimal = this.valueDecimal,
                    valueString = this.valueString,
                    valueBoolean = this.valueBoolean,
                    valueInteger = this.valueInteger,
                    valueCoding = this.valueCoding?.let { convert(it) },
                    valueQuantity = this.valueQuantity?.let { convertValueQuantity(it) },
                    valueAttachment = this.valueAttachment?.let { convertValueAttachment(it) }
                )
            }
        }

        private fun convertEnableWhenList(enableWhenDataList: List<EnableWhenData>): List<DynQuestionEntity.EnableWhen?> =
            enableWhenDataList.map { convertEnableWhen(it) }

        private fun convertEnableWhen(it: EnableWhenData): DynQuestionEntity.EnableWhen? {
            with(it) {
                answerBoolean?.run {
                    return DynQuestionEntity.EnableWhenBoolean(
                        operator,
                        question,
                        this
                    )
                }
                answerInteger?.run {
                    return DynQuestionEntity.EnableWhenInteger(
                        operator,
                        question,
                        this
                    )
                }
                answerCoding?.run {
                    return DynQuestionEntity.EnableWhenOpenChoice(
                        operator,
                        question,
                        convert(this)
                    )
                }
                answerQuantity?.run {
                    return DynQuestionEntity.EnableWhenFloat(
                        operator,
                        question,
                        this.value
                    )
                }
                answerString?.run {
                    return DynQuestionEntity.EnableWhenString(
                        operator,
                        question,
                        this
                    )
                }
                return null
            }
        }

        fun convert(valueCodingData: ValueCodingData) = with(valueCodingData) {
            DynQuestionEntity.ValueCodingEntity(
                code,
                display,
                version = version,
                system = system
            )
        }

        fun convert(valueRange: ValueRange): DynQuestionEntity.ValueRangeEntity = with(valueRange){
            DynQuestionEntity.ValueRangeEntity(high = high, low = low)
        }

        fun convert(
            it: List<ItemData>,
            authored: String,
            extension: List<ExtensionData>
        ): DynQuestListEntity.QuestFilledEntity {
            val questions = mutableListOf<DynQuestionEntity>()

            for (item in it) {
                val question = convert(item)
                questions.add(question)
                if (item.item.isNotEmpty()) {
                    questions.addAll(item.item.map { convert(it, question) })
                }
            }

            return DynQuestListEntity.QuestFilledEntity(
                date = authored,
                questions = questions,
                description = getDescription(extension)
            )
        }

        fun convert(it: SendMonitoringAnswersResponseData): DynQuestListEntity.QuestFilledEntity {
            with(it) {
                val questions = mutableListOf<DynQuestionEntity>()

                for (item in item) {
                    val question = convert(item)
                    questions.add(question)
                    if (item.item.isNotEmpty()) {
                        questions.addAll(item.item.map { convert(it, question) })
                    }
                }

                return DynQuestListEntity.QuestFilledEntity(
                    date = this.authored,
                    questions = questions,
                    description = getDescription(extension)
                )
            }
        }

        fun convertToItemData(
            questionEntity: DynQuestionEntity,
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
            group: DynQuestionEntity
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

        private fun convertValueQuantity(valueQuantity: DynQuestionEntity.ValueQuantity): ValueQuantity {
            valueQuantity.apply {
                return ValueQuantity(value = value, unit = unit)
            }
        }

        private fun convertValueQuantity(valueQuantity: ValueQuantity): DynQuestionEntity.ValueQuantity {
            valueQuantity.apply {
                return DynQuestionEntity.ValueQuantity(value = value, unit = unit)
            }
        }

        private fun convertValueAttachment(valueAttachment: DynQuestionEntity.ValueAttachment): ValueAttachment {
            valueAttachment.apply {
                return ValueAttachment(
                    title = title,
                    data = data,
                    size = size,
                    creation = creation
                )
            }
        }

        private fun convertValueAttachment(valueAttachment: ValueAttachment): DynQuestionEntity.ValueAttachment {
            valueAttachment.apply {
                return DynQuestionEntity.ValueAttachment(
                    title = title,
                    data = data,
                    size = size,
                    creation = creation
                )
            }
        }

        private fun convertAnyToAnswer(questionEntity: DynQuestionEntity, any: Any) =
            when (questionEntity) {
                is DynQuestionEntity.TextQuestionEntity -> AnswerData(valueString = any.toString())
                is DynQuestionEntity.UrlQuestionEntity -> AnswerData(valueUri = any.toString())
                is DynQuestionEntity.BooleanQuestionEntity -> AnswerData(valueBoolean = any as Boolean)
                is DynQuestionEntity.IntegerQuestionEntity -> AnswerData(valueInteger = any.toString())
                is DynQuestionEntity.DecimalQuestionEntity -> AnswerData(valueDecimal = any.toString())
                is DynQuestionEntity.DateQuestionEntity -> AnswerData(valueDate = any.toString())
                is DynQuestionEntity.DateTimeQuestionEntity -> AnswerData(valueDateTime = any.toString())
                is DynQuestionEntity.TimeQuestionEntity -> AnswerData(valueTime = any.toString())
                is DynQuestionEntity.ChoicesQuestionEntity -> AnswerData(valueString = any.toString())
                is DynQuestionEntity.OpenChoicesQuestionEntity -> AnswerData(valueString = any.toString())
                is DynQuestionEntity.QuantityQuestionEntity -> AnswerData(valueQuantity = convertValueQuantity(any as DynQuestionEntity.ValueQuantity))
                is DynQuestionEntity.AttachmentQuestionEntity -> AnswerData(valueAttachment = convertValueAttachment((any as DynQuestionEntity.ValueAttachment)))
                else -> AnswerData(valueString = any.toString())
            }

        private fun getQuestionType(questionEntity: DynQuestionEntity): String =
            when (questionEntity) {
                is DynQuestionEntity.TextQuestionEntity -> DynQuestionEntity.QuestionType.TEXT.nameStr
                is DynQuestionEntity.UrlQuestionEntity -> DynQuestionEntity.QuestionType.URL.nameStr
                is DynQuestionEntity.BooleanQuestionEntity -> DynQuestionEntity.QuestionType.BOOLEAN.nameStr
                is DynQuestionEntity.IntegerQuestionEntity -> DynQuestionEntity.QuestionType.INTEGER.nameStr
                is DynQuestionEntity.DecimalQuestionEntity -> DynQuestionEntity.QuestionType.DECIMAL.nameStr
                is DynQuestionEntity.DateQuestionEntity -> DynQuestionEntity.QuestionType.DATE.nameStr
                is DynQuestionEntity.DateTimeQuestionEntity -> DynQuestionEntity.QuestionType.DATETIME.nameStr
                is DynQuestionEntity.TimeQuestionEntity -> DynQuestionEntity.QuestionType.TIME.nameStr
                is DynQuestionEntity.ChoicesQuestionEntity -> DynQuestionEntity.QuestionType.CHOICE.nameStr
                is DynQuestionEntity.OpenChoicesQuestionEntity -> DynQuestionEntity.QuestionType.OPEN_CHOICE.nameStr
                is DynQuestionEntity.QuantityQuestionEntity -> DynQuestionEntity.QuestionType.QUANTITY.nameStr
                is DynQuestionEntity.AttachmentQuestionEntity -> DynQuestionEntity.QuestionType.ATTACHMENT.nameStr
                else -> DynQuestionEntity.QuestionType.NOT_SUPPORTED.nameStr
            }

        private fun convertValueCoding(extensionList: List<ExtensionData>?): List<DynQuestionEntity.ValueCodingExtension>? {
            val result = mutableListOf<DynQuestionEntity.ValueCodingExtension>()
            extensionList?.let { extensionDataList ->
                if (isValueCodingExtensionHelp(extensionDataList)) {
                    for (extensionData in extensionDataList) {
                        result.add(
                            DynQuestionEntity.ValueCodingExtension(
                                code = extensionData.valueCoding.code,
                                display = extensionData.valueCoding.display,
                                url = extensionData.url
                            )
                        )
                    }
                    return result
                }
            }
            return null
        }

        private fun isValueCodingExtensionHelp(extensionList: List<ExtensionData>?): Boolean {
            var result = false
            extensionList?.forEach { extensionData ->
                if(extensionData.url == "tipo" && extensionData.valueCoding.code == "Ayuda"){
                    result = true
                }
            }
            return result
        }



    }
}
