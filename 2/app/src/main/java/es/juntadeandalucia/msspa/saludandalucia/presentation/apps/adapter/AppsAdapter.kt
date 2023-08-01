package es.juntadeandalucia.msspa.saludandalucia.presentation.apps.adapter

import android.content.Context
import android.view.View
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.view_item_app.view.*

class AppsAdapter(
    listener: (AppEntity, View) -> Unit,
    val buttonListener: (Context, AppEntity) -> Unit
) :
    BaseListAdapter<AppEntity>(onClickItemListener = listener) {

    override val itemRowResource = R.layout.view_item_app

    override fun bind(itemView: View, item: AppEntity, listener: (AppEntity, View) -> Unit) {
        itemView.apply {
            setOnClickListener { listener(item, itemView) }
            Picasso.get()
                .load(item.icon)
                .placeholder(R.drawable.img_news_placeholder)
                .into(app_icon_iv)
            app_name_tv.text = item.name
            app_description_tv.text = item.description
            app_category_tv.text = item.category
            if (Utils.checkAppInstalled(context, item.packageName)) {
                apps_btn.setText(R.string.btn_open)
            } else {
                apps_btn.setText(R.string.btn_download)
            }
            apps_btn.setOnClickListener {
                buttonListener(context, item)
            }

            app_name_tv.transitionName = item.packageName + item.name
            app_icon_iv.transitionName = item.packageName + item.icon
            app_description_tv.transitionName = item.packageName + item.description
            app_category_tv.transitionName = item.packageName + item.category
            apps_btn.transitionName = item.packageName + "download_button"
        }
    }

    fun showSearchResults(list: List<AppEntity>) {
        setItemsAndNotify(list)
    }
}
