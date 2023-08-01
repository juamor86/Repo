package es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class MonitoringProgramQuestionAnwseredEntity(
    open val questionId: String,
    open val question: String,
    open val options: List<String> = emptyList(),
    open val answerBoolean: Boolean = false,
    open val answerText: String = ""
) : Parcelable {

    @Parcelize
    data class BooleanQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val answerBoolean: Boolean
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        answerBoolean = answerBoolean
    )

    @Parcelize
    data class BooleanExtQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val answerBoolean: Boolean
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        answerBoolean = answerBoolean
    )

    @Parcelize
    data class DecimalQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val answerText: String
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        answerText = answerText
    )

    @Parcelize
    data class TextQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val answerText: String
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        answerText = answerText
    )

    @Parcelize
    data class OptionsQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val options: List<String>
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        options = options
    )

    @Parcelize
    data class SingleOptionButtonQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val options: List<String>
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        options = options
    )

    @Parcelize
    data class MultipleOptionsQuestionEntity(
        override val questionId: String,
        override val question: String,
        override val options: List<String>
    ) : Parcelable, MonitoringProgramQuestionAnwseredEntity(
        questionId = questionId,
        question = question,
        options = options
    )
}
