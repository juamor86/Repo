package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list.adapter

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import kotlinx.android.synthetic.main.view_monitoring_program_item.view.*

class MonitoringListAdapter(listener: (MonitoringListEntity.QuestFilledEntity, View) -> Unit) :
    BaseListAdapter<MonitoringListEntity.QuestFilledEntity>(onClickItemListener = listener) {
    override val itemRowResource: Int = R.layout.view_monitoring_program_item

    override fun bind(
        itemView: View,
        item: MonitoringListEntity.QuestFilledEntity,
        listener: (MonitoringListEntity.QuestFilledEntity, View) -> Unit
    ) {
        itemView.apply {
            setOnClickListener { listener(item, itemView) }
            val date = UtilDateFormat.stringToDate(item.date, UtilDateFormat.DATE_FORMAT_TZ)
            monitoring_date_tv.text = UtilDateFormat.dateToStringMonthName(date)
            monitoring_time_tv.text = UtilDateFormat.timeToString(date).plus(" h")
        }
    }
}
