package es.inteco.conanmobile.presentation.osi

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.view_item_warning.view.*

/**
 * O s i adapter
 *
 * @constructor Create empty O s i adapter
 */
class OSIAdapter : BaseListAdapter<OSIEntity>() {
    override val itemRowResource = R.layout.view_item_osi

    override fun bind(
        itemView: View, item: OSIEntity, listener: (OSIEntity) -> Unit
    ) {
        with(itemView) {
            title_tv.apply {
                text = Html.fromHtml(item.title.values.firstOrNull() ?: "")
                movementMethod = LinkMovementMethod.getInstance()
            }
            description_tv.apply {
                text = Html.fromHtml(item.description.values.firstOrNull() ?: "")
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}