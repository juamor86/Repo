package es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonitoringListEntity(
    val questsFilled: MutableList<QuestFilledEntity>
) : Parcelable {
    @Parcelize
    data class QuestFilledEntity(
        val date: String,
        val description: String,
        val questions: MutableList<QuestionEntity>
    ) : Parcelable
}
