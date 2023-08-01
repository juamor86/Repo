package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.adapters

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementValueEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseAdapter
import kotlinx.android.synthetic.main.view_square_datetime.view.*

class DetailAdapter : BaseAdapter<MeasurementValueEntity>() {

    override val itemRowResource: Int = R.layout.view_item_measure

    override fun bind(
        itemView: View,
        item: MeasurementValueEntity,
        listener: (MeasurementValueEntity) -> Unit
    ) {
        with(itemView) {
            date_tv.text = item.date
            time_tv.text = item.hour

            val linearLayout = itemView.findViewById<LinearLayout>(R.id.values_ll)

            if (linearLayout.childCount == 0){
                for (i in item.values.indices) {
                    val childView =
                        LayoutInflater.from(context).inflate(R.layout.view_item_measure_value, null)
                    childView.findViewById<TextView>(R.id.measure_value_tv).text =
                        resources.getString(
                            R.string.measurement_value,
                            item.values[i],
                            item.units[i]
                        )
                    linearLayout.addView(childView)
                }
            }

        }
    }
}
