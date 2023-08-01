package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.adapter

import android.view.View
import androidx.core.text.HtmlCompat
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import kotlinx.android.synthetic.main.view_item_notification.view.*

class NotificationsAdapter(
    onClickItemListener: (NotificationEntity, View) -> Unit,
    onRemoveItemListener: (NotificationEntity) -> Unit
) : BaseListAdapter<NotificationEntity>(
    onClickItemListener = onClickItemListener,
    onRemoveItemListener = onRemoveItemListener
) {

    override val itemRowResource = R.layout.view_item_notification

    override fun bind(
        itemView: View,
        item: NotificationEntity,
        listener: (NotificationEntity, View) -> Unit
    ) {
        with(itemView) {
            notification_title_tv.text = item.title
            notification_description_tv.text =
                item.description.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
            if (item.readed) {
                notification_readed_vw.background = resources.getDrawable(R.drawable.sh_notification_rounded_readed, null)
                notification_date_tv.setTextColor(resources.getColor(R.color.dark_grey))
                notification_title_tv.setTextColor(resources.getColor(R.color.dark_grey))
                notification_description_tv.setTextColor(resources.getColor(R.color.dark_grey))
            }
            notification_date_tv.text = UtilDateFormat.getDateFormatterNotification(item.date).toLowerCase().replace(".", "")
            setOnClickListener { listener(item, itemView) }
        }
    }
}
