package es.inteco.conanmobile.presentation.analysis.results.apps

import android.view.View
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.settings_attention_list_item.view.*

/**
 * Apps adapter
 *
 * @property malicious
 * @constructor
 *
 * @param onClickItemListener
 */
class AppsAdapter(onClickItemListener: (ApplicationEntity) -> Unit, val malicious: Boolean) :
    BaseListAdapter<ApplicationEntity>(onClickItemListener = onClickItemListener) {

    override val itemRowResource = R.layout.apps_list_item

    override fun bind(
        itemView: View, item: ApplicationEntity, listener: (ApplicationEntity) -> Unit
    ) {
        with(itemView) {
            setOnClickListener { listener(item) }
            if (malicious) {
                app_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            } else {
                app_cv.setCardBackgroundColor(resources.getColor(R.color.noIncidence))
            }
            app_name_tv.text = item.name
        }
    }

    override fun setItemsAndNotify(list: List<ApplicationEntity>) {
        val mutable = list.toMutableList()
        super.setItemsAndNotify(mutable)
    }
}