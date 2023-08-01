package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires

import android.os.Parcelable
import androidx.core.text.isDigitsOnly
import kotlinx.android.parcel.Parcelize

@Parcelize
open class DynQuestionEntity(
    open val questionId: String,
    open val question: String,
    open val mandatory: Boolean,
    open val enableWhen: EnableWhen? = null,
    open val answer: AnswerOptionEntity? = null,
    open var group: DynQuestionEntity? = null
) : Parcelable {

    var error = false

    open fun getIntType() = 0

    open fun checkResponse(response: Any): Boolean {
        return true
    }

    enum class QuestionType(val nameStr: String) {
        BOOLEAN("boolean"), DECIMAL("decimal"), URL("url"), ATTACHMENT("attachment"),
        INTEGER("integer"), TEXT("text"), DATE("date"), TIME("time"), DATETIME("dateTime"), CHOICE("choice"),
        OPEN_CHOICE("open-choice"), QUANTITY("quantity"), GROUP("group"), NOT_SUPPORTED("not_supported")
    }

    @Parcelize
    data class NotSupportedQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null,
        val valueCodingExtension: List<ValueCodingExtension>? = null


    ) : Parcelable, DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.NOT_SUPPORTED.ordinal
    }

    @Parcelize
    data class BooleanQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null

    ) : Parcelable, DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.BOOLEAN.ordinal
    }

    @Parcelize
    data class DecimalQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        val minValue: Float? = null,
        val maxValue: Float? = null,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.DECIMAL.ordinal
    }

    @Parcelize
    data class IntegerQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        val minValue: Int? = null,
        val maxValue: Int? = null,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.INTEGER.ordinal

        override fun checkResponse(response: Any): Boolean {
            val inputInt =
                if (response.toString().isEmpty()) 0 else Integer.parseInt(response.toString())
            if ((minValue != null && inputInt < minValue) || (maxValue != null && inputInt > maxValue)) {
                return false
            }
            return true
        }
    }

    @Parcelize
    data class TextQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        val maxLength: Int? = null,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.TEXT.ordinal
    }

    @Parcelize
    data class UrlQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.URL.ordinal

        override fun checkResponse(response: Any): Boolean {
            val URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]".toRegex()
            val url = response.toString()
            return URL_REGEX.matches(url)
        }
    }

    @Parcelize
    data class DateQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.DATE.ordinal
    }

    @Parcelize
    data class TimeQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.TIME.ordinal
    }

    @Parcelize
    data class AttachmentQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.ATTACHMENT.ordinal
    }

    @Parcelize
    data class ValueAttachment(
        val title: String = "",
        val data: String = "",
        val size: Long = 0,
        val creation: String = ""
    ) : Parcelable

    @Parcelize
    data class DateTimeQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.DATETIME.ordinal
    }

    @Parcelize
    data class ChoicesQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        val options: List<AnswerOptionEntity>? = null,
        val valueRange: ValueRangeEntity? = null,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.CHOICE.ordinal

        override fun checkResponse(response: Any): Boolean {
            val realResponse = response as List<*>
            return if (valueRange != null) {
                realResponse.size in valueRange.low..valueRange.high
            } else {
                false
            }
        }
    }

    @Parcelize
    data class OpenChoicesQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        val valueRange: ValueRangeEntity? = null,
        val options: List<AnswerOptionEntity> = listOf(),
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.OPEN_CHOICE.ordinal

        override fun checkResponse(response: Any): Boolean {
            val realResponse = response as List<*>
            return if (valueRange != null) {
                realResponse.size in valueRange.low..valueRange.high
            } else {
                false
            }
        }
    }

    @Parcelize
    data class QuantityQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        val minValue: Int? = null,
        val maxValue: Int? = null,
        val unit: String = "",
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen,
        answer = answer
    ) {
        override fun getIntType() = QuestionType.QUANTITY.ordinal
    }

    @Parcelize
    data class AnswerOptionEntity(
        val valueCoding: ValueCodingEntity? = null,
        val valueQuantity: ValueQuantity? = null,
        val valueAttachment: ValueAttachment? = null,
        val valueDate: String? = null,
        val valueInteger: String? = null,
        val valueString: String? = null,
        val valueUri: String? = null,
        val valueTime: String? = null,
        val valueDateTime: String? = null,
        val valueBoolean: Boolean? = null,
        val valueDecimal: String? = null,
        var selected: Boolean = false,
        var isOpenChiceText :Boolean = false
    ) : Parcelable {
        fun getTextValue(): String {
            when {
                valueTime != null -> return valueTime
                valueUri != null -> return valueUri
                valueDate != null -> return valueDate
                valueInteger != null -> {
                    if (valueInteger == "0") {
                        return valueInteger
                    }
                    return valueInteger.trimStart('0')
                }
                valueString != null -> return valueString
                valueDecimal != null -> return valueDecimal.toString()
            }
            return ""
        }
    }

    @Parcelize
    data class ValueRangeEntity(
        val high: Int = 1,
        val low: Int = 1
    ) : Parcelable

    @Parcelize
    data class GroupQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val enableWhen: EnableWhen? = null,
        override val answer: AnswerOptionEntity? = null
    ) : DynQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        enableWhen = enableWhen
    ) {
        override fun getIntType() = QuestionType.GROUP.ordinal
    }

    @Parcelize
    data class ValueCodingEntity(
        val code: String = "",
        val display: String = "",
        val system: String = "",
        val version: String = ""
    ) : Parcelable {
        var checked: Boolean = false
    }

    @Parcelize
    data class ValueQuantity(
        val value: String = "",
        val unit: String = ""
    ) : Parcelable

    @Parcelize
    open class EnableWhen(
        open val operator: String = "",
        open val question: String = ""
    ) : Parcelable {
        open fun isEnabled(response: Any): Boolean? {
            return false
        }
    }

    @Parcelize
    class EnableWhenBoolean(
        override val operator: String,
        override val question: String,
        val answerBoolean: Boolean
    ) :
        EnableWhen(operator, question), Parcelable {

        override fun isEnabled(response: Any): Boolean =
            when (operator) {
                "==" -> if (response is Boolean) response else false
                else -> false
            }
    }

    @Parcelize
    class EnableWhenString(
        override val operator: String,
        override val question: String,
        val answer: String
    ) :
        EnableWhen(operator, question), Parcelable {

        override fun isEnabled(response: Any): Boolean =
            when (operator) {
                "==" -> comparate(response)
                else -> false
            }

        private fun comparate(response: Any): Boolean {
            return when (response) {
                is ArrayList<*> -> {
                    response.contains(answer)
                }
                is String -> {
                    response == answer
                }
                else -> false
            }
        }

    }

    @Parcelize
    class EnableWhenInteger(
        override val operator: String,
        override val question: String,
        private val answerInteger: Int
    ) :
        EnableWhen(operator, question), Parcelable {

        override fun isEnabled(response: Any): Boolean {
            if (response.toString().run { isDigitsOnly().and(isNotEmpty()) }) {
                val responseInt = response.toString().toInt()
                when (operator) {
                    ">" -> return responseInt > answerInteger
                    ">=" -> return responseInt >= answerInteger
                    "<" -> return responseInt < answerInteger
                    "<=" -> return responseInt <= answerInteger
                    "==" -> return responseInt == answerInteger
                }
            }
            return false
        }
    }

    @Parcelize
    class EnableWhenFloat(
        override val operator: String,
        override val question: String,
        private val answerFloat: Float
    ) :
        EnableWhen(operator, question), Parcelable {

        override fun isEnabled(response: Any): Boolean {
            if (response is String && response.matches(Regex("[.0-9]+"))) {
                when (operator) {
                    ">" -> return response.toFloat() > answerFloat
                    ">=" -> return response.toFloat() >= answerFloat
                    "<" -> return response.toFloat() < answerFloat
                    "<=" -> return response.toFloat() <= answerFloat
                    "==" -> return response.toFloat() == answerFloat
                }
            }
            return false
        }
    }

    @Parcelize
    class EnableWhenOpenChoice(
        override val operator: String,
        override val question: String,
        private val answerOptionEntity: ValueCodingEntity
    ) :
        EnableWhen(operator, question), Parcelable {

        override fun isEnabled(response: Any): Boolean? {
            return if (response is ValueCodingEntity && response.code == answerOptionEntity.code) {
                response.checked
            } else {
                null
            }
        }
    }

    @Parcelize
    data class ValueCodingExtension(
        val code: String? = null,
        val display: String? = null,
        val url: String? = null
    ) : Parcelable
}
