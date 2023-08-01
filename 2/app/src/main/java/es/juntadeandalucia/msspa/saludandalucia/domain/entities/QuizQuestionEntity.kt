package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class QuizQuestionEntity(
    open val questionId: String,
    open val question: String,
    open val mandatory: Boolean,
    open val minValue: Int = 0,
    open val maxValue: Int = 0,
    open val maxLength: Int = 0,
    open val cardinality: String = ""
) : Parcelable {
    @Parcelize
    data class BooleanQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean
    ) : Parcelable, QuizQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory
    )

    @Parcelize
    data class BooleanExtQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean
    ) : QuizQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory
    )

    @Parcelize
    data class DecimalQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val minValue: Int,
        override val maxValue: Int
    ) : QuizQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        minValue = minValue,
        maxValue = maxValue
    )

    @Parcelize
    data class TextQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val maxLength: Int
    ) : QuizQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        maxLength = maxLength
    )

    @Parcelize
    data class OptionsQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val mandatory: Boolean,
        override val cardinality: String,
        val options: List<String>
    ) : QuizQuestionEntity(
        questionId = questionId,
        question = question,
        mandatory = mandatory,
        cardinality = cardinality
    )
}
