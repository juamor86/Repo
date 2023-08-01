package es.inteco.conanmobile.presentation.analysis.type

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.default_analysis_list_item.view.*

/**
 * Analysis type adapter
 *
 * @property context
 * @property selectedAnalysis
 * @constructor
 *
 * @param onClickItemListener
 * @param onRemoveItemListener
 */
class AnalysisTypeAdapter(
    val context: Context,
    var selectedAnalysis: AnalysisEntity,
    onClickItemListener: (AnalysisEntity) -> Unit,
    onRemoveItemListener: (AnalysisEntity) -> Unit
) : BaseListAdapter<AnalysisEntity>(
        onClickItemListener = onClickItemListener,
        onRemoveItemListener = onRemoveItemListener) {

    override val itemRowResource = R.layout.default_analysis_list_item

    var itemViewList: MutableList<View> = mutableListOf()

    override fun bind(
        itemView: View,
        item: AnalysisEntity,
        listener: (AnalysisEntity) -> Unit
    ) {
        itemViewList.add(itemView)

        with(itemView) {
            setOnClickListener {
                unselectAllItems()
                selectedAnalysis = item
                radioButton.isChecked = true
                clType.background = ResourcesCompat.getDrawable(getResources(), R.drawable.sh_analysis_item_selected, getContext().getTheme())
                listener(item)
            }
            radioButton.setOnClickListener {
                unselectAllItems()
                selectedAnalysis = item
                radioButton.isChecked = true
                clType.background = ResourcesCompat.getDrawable(getResources(), R.drawable.sh_analysis_item_selected, getContext().getTheme())
                listener(item)
            }
            radioButton.buttonTintList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    Color.BLACK,  // disabled
                    context.getColor(R.color.card_title_analysis) // enabled
                )
            )
            radioButton.isChecked = false
            clType.background = ResourcesCompat.getDrawable(getResources(), R.drawable.sh_analysis_item, getContext().getTheme())
            itemView.rv_title.text = item.names[0].value
            itemView.rv_subtitle.text = item.descriptions[0].value

            if (item == selectedAnalysis) {
                radioButton.isChecked = true
                clType.background = ResourcesCompat.getDrawable(getResources(), R.drawable.sh_analysis_item_selected, getContext().getTheme())
            }
        }
    }

    private fun unselectAllItems() {
        itemViewList.forEach { itemView ->
            with(itemView) {
                radioButton.isChecked = false
                clType.background = ResourcesCompat.getDrawable(getResources(), R.drawable.sh_analysis_item, getContext().getTheme())
            }
        }
    }

    override fun setItemsAndNotify(list: List<AnalysisEntity>) {
        val mutable = list.toMutableList()
        super.setItemsAndNotify(mutable)
    }
}