package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list.adapter

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import kotlinx.android.synthetic.main.view_dyn_quest_item.view.*

class DynQuestListAdapter(listener: (DynQuestListEntity.QuestFilledEntity, View) -> Unit) :
    BaseListAdapter<DynQuestListEntity.QuestFilledEntity>(onClickItemListener = listener) {
    override val itemRowResource: Int = R.layout.view_dyn_quest_item

    override fun bind(
        itemView: View,
        item: DynQuestListEntity.QuestFilledEntity,
        listener: (DynQuestListEntity.QuestFilledEntity, View) -> Unit
    ) {
        itemView.apply {
            setOnClickListener { listener(item, itemView) }
            val date = UtilDateFormat.stringToDate(item.date, UtilDateFormat.DATE_FORMAT_TZ)
            questionnaire_date_tv.text = UtilDateFormat.dateToStringMonthName(date)
            questionnaire_time_tv.text = UtilDateFormat.timeToString(date).plus(" h")
        }
    }
}
