package es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DynQuestListEntity(
    val questsFilled: MutableList<QuestFilledEntity>
) : Parcelable {
    @Parcelize
    data class QuestFilledEntity(
        val date: String,
        val description: String,
        val questions: MutableList<DynQuestionEntity>
    ) : Parcelable
}
