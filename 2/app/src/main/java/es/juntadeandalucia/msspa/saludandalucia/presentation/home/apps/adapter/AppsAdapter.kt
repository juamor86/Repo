package es.juntadeandalucia.msspa.saludandalucia.presentation.home.apps.adapter

import android.view.View
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.view_item_home_app.view.*

class AppsAdapter(listener: (app: AppEntity, itemView: View) -> Unit) :
    BaseListAdapter<AppEntity>(onClickItemListener = listener) {

    override val itemRowResource = R.layout.view_item_home_app

    override fun bind(itemView: View, item: AppEntity, listener: (AppEntity, View) -> Unit) {
        with(itemView) {
            Picasso.get()
                .load(item.icon)
                .placeholder(R.drawable.img_news_placeholder)
                .into(app_home_icon_iv)
            setOnClickListener { listener(item, itemView) }

            app_home_cv.transitionName = item.packageName + item.packageName
            app_home_icon_iv.transitionName = item.packageName + item.icon
        }
    }
}
