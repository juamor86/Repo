package es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details.image.adapter

import android.view.View
import android.widget.ImageView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter

class ImagesAdapter(listener: (String, View) -> Unit) : BaseListAdapter<String>(onClickItemListener = listener) {
    override val itemRowResource: Int = R.layout.view_item_app_details_image

    override fun bind(itemView: View, item: String, listener: (String, View) -> Unit) {
        with(itemView as ImageView) {
            val iconResourceId = context.resources.getIdentifier(
                item,
                "drawable",
                context.packageName
            )

            if (iconResourceId > 0) {
                setImageResource(iconResourceId)
            }

            setOnClickListener { listener(item, itemView) }
        }
    }
}
