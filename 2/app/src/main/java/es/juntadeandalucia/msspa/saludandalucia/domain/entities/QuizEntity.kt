package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizEntity(
    val available: Boolean,
    val nextTry: String,
    val questions: List<QuizQuestionEntity>,
    val appointment: AppointmentEntity?
) : Parcelable
