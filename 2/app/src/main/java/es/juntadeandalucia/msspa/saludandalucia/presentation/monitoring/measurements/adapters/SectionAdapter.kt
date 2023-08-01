package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.adapters

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseAdapter
import kotlinx.android.synthetic.main.view_item_measurement_section.view.*
import kotlinx.android.synthetic.main.view_item_measurement_title_header.view.*

class SectionAdapter(
    listener: (MeasurementSectionEntity) -> Unit
) : BaseAdapter<MeasurementSectionEntity>(listener = listener) {

    override val itemRowResource: Int = R.layout.view_item_measurement_section

    override fun bind(
        itemView: View,
        item: MeasurementSectionEntity,
        listener: (MeasurementSectionEntity) -> Unit
    ) {
        itemView.apply {
            setOnClickListener { listener(item) }
            measure_title.text = item.title
            measure_description.text = item.range

            if (item.headers.size <= 1) {
                column_ll.visibility = View.GONE
            } else {
                column_ll.removeAllViews()
                for (header in item.headers) {
                    val childView =
                        LayoutInflater.from(context)
                            .inflate(
                                R.layout.view_item_measurement_column_header,
                                null
                            )

                    childView.findViewById<TextView>(R.id.column_header_tv).text = header
                    column_ll.addView(childView)
                }
            }

            measurements_ll.removeAllViews()
            for (value in item.values.take(3)) {
                val childView: View =
                    LayoutInflater.from(context).inflate(R.layout.view_item_measure, null)

                val linearLayout = childView.findViewById<LinearLayout>(R.id.values_ll)

                childView.findViewById<TextView>(R.id.date_tv).text = value.date
                childView.findViewById<TextView>(R.id.time_tv).text = value.hour

                for (i in value.values.indices) {
                    val valueView =
                        LayoutInflater.from(context).inflate(R.layout.view_item_measure_value, null)

                    valueView.findViewById<TextView>(R.id.measure_value_tv).text =
                        resources.getString(
                            R.string.measurement_value,
                            value.values[i],
                            value.units[i]
                        )
                    linearLayout.addView(valueView)
                }

                measurements_ll.addView(childView)
            }
        }
    }
}
