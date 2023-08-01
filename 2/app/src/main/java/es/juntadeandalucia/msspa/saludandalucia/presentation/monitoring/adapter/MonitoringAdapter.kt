package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.adapter

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.item_dyn_button.view.*

class MonitoringAdapter(listener: (MonitoringEntity.MonitoringEntry, View) -> Unit) :
    BaseListAdapter<MonitoringEntity.MonitoringEntry>(onClickItemListener = listener) {

    override val itemRowResource: Int = R.layout.item_dyn_button

    override fun bind(
        itemView: View,
        item: MonitoringEntity.MonitoringEntry,
        listener: (MonitoringEntity.MonitoringEntry, View) -> Unit
    ) {
        itemView.apply {
            setOnClickListener { listener(item, itemView) }
            title_tv.text = item.title
            image_iv.setImageResource(if (item.type == Consts.TYPE_MONITORING_PROGRAM) R.drawable.ll_follow_up else R.drawable.ll_measurements)
        }
    }
}
