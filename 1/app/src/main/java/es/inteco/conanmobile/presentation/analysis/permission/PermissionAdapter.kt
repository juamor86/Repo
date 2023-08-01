package es.inteco.conanmobile.presentation.analysis.permission

import android.view.View
import es.inteco.conanmobile.R
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.incidence_detail_list_item.view.*

/**
 * Permission adapter
 *
 * @constructor
 *
 * @param onClickItemListener
 * @param onRemoveItemListener
 */
class PermissionAdapter(
    onClickItemListener: (String) -> Unit,
    onRemoveItemListener: (String) -> Unit
) : BaseListAdapter<String>(
        onClickItemListener = onClickItemListener,
        onRemoveItemListener = onRemoveItemListener
    ) {

    override val itemRowResource = R.layout.incidence_detail_list_item

    override fun bind(itemView: View, item: String, listener: (String) -> Unit) {
        with(itemView) {
            incidence_tv.text = item

            setOnClickListener {
                listener(item)
            }
        }
    }

    override fun setItemsAndNotify(list: List<String>) {
        val mutable = list.toMutableList()
        super.setItemsAndNotify(mutable)
    }
}