package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import androidx.annotation.DrawableRes
import es.juntadeandalucia.msspa.saludandalucia.R
import kotlinx.android.parcel.Parcelize

sealed class QuizResultEntity(
    open val result: String,
    @DrawableRes val headerResId: Int,
    open val nextTryMillis: Long,
    open val appointment: AppointmentEntity?

) : Parcelable {

    @Parcelize
    data class QuizResultPositiveEntity(
        override val result: String,
        override val nextTryMillis: Long,
        override val appointment: AppointmentEntity?
    ) : QuizResultEntity(
        result,
        R.drawable.ic_quiz_result_positive_header,
        nextTryMillis,
        appointment
    )

    @Parcelize
    data class QuizResultNegativeEntity(
        override val result: String,
        override val nextTryMillis: Long,
        override val appointment: AppointmentEntity?
    ) : QuizResultEntity(
        result,
        R.drawable.ic_quiz_result_negative_header,
        nextTryMillis,
        appointment
    )
}
