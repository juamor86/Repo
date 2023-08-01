package es.inteco.conanmobile.presentation.warnings

import android.view.View
import androidx.core.content.ContextCompat
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import es.inteco.conanmobile.utils.Utils
import kotlinx.android.synthetic.main.view_item_warning.view.*
import java.util.*

/**
 * Warnings adapter
 *
 * @constructor Create empty Warnings adapter
 */
class WarningsAdapter : BaseListAdapter<WarningEntity>(
) {
    override val itemRowResource = R.layout.view_item_warning

    override fun bind(
        itemView: View,
        item: WarningEntity,
        listener: (WarningEntity) -> Unit
    ) {
        with(itemView) {
            title_tv.text = item.title
            description_tv.text = item.description
            date_tv.text = Utils.dateToString(Date(item.date), "dd/MM/yyyy")
            when (item.importance) {
                WarningEntity.Importance.HIGH -> {
                    importance_tv.text = resources.getString(R.string.importance_high)
                    importance_v.background = ContextCompat.getDrawable(context, R.color.red)
                }
                WarningEntity.Importance.MEDIUM -> {
                    importance_tv.text = resources.getString(R.string.importance_medium)
                    importance_v.background = ContextCompat.getDrawable(context, R.color.yellow)
                }
                WarningEntity.Importance.LOW -> {
                    importance_tv.text = resources.getString(R.string.importance_low)
                    importance_v.background = ContextCompat.getDrawable(context, R.color.gray)
                }
            }
        }
    }
}