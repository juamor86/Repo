package es.inteco.conanmobile.presentation.analysis.results.settings

import android.view.View
import androidx.core.view.isVisible
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.settings_attention_list_item.view.*

/**
 * Critical settings adapter
 *
 * @property onButtonClickListener
 * @constructor
 *
 * @param onClickItemListener
 * @param onRemoveItemListener
 */
class CriticalSettingsAdapter(
    onClickItemListener: (ModuleResultEntity) -> Unit,
    val onButtonClickListener: (ModuleResultEntity) -> Unit,
    onRemoveItemListener: (ModuleResultEntity) -> Unit
) : BaseListAdapter<ModuleResultEntity>(
    onClickItemListener = onClickItemListener, onRemoveItemListener = onRemoveItemListener
) {

    override val itemRowResource = R.layout.settings_attention_list_item

    override fun bind(
        itemView: View,
        item: ModuleResultEntity,
        listener: (ModuleResultEntity) -> Unit
    ) {
        with(itemView) {
            app_name_tv.text = item.item.names[0].value
            when (item.item.assessment.criticality) {
                "GOOD" -> app_cv.setCardBackgroundColor(resources.getColor(R.color.noIncidence))
                "NEUTRAL" -> app_cv.setCardBackgroundColor(resources.getColor(R.color.review))
                "CRITICAL" -> app_cv.setCardBackgroundColor(resources.getColor(R.color.incidence))
            }
            itemView.setOnClickListener { listener(item) }
            with(arrow_iv) {
                isVisible = item.shouldLaunchAction
                arrow_iv.setOnClickListener {
                    onButtonClickListener(item)
                }
            }
            alert_new_iv.isVisible = item.previousAnalysisDifferent
        }
    }

    override fun setItemsAndNotify(list: List<ModuleResultEntity>) {
        val mutable = list.toMutableList()
        super.setItemsAndNotify(mutable)
    }
}