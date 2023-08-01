package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types.adapter

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.getAdviceFromTypeCriteria
import kotlinx.android.synthetic.main.view_item_advice_type.view.*

class NewAdviceTypeAdapter(
    onClickItemListener: (AdviceTypeResource, View) -> Unit,
    onRemoveItemListener: (AdviceTypeResource) -> Unit,
    private val advices: List<AdviceEntity>
) : BaseListAdapter<AdviceTypeResource>(
    onClickItemListener = onClickItemListener,
    onRemoveItemListener = onRemoveItemListener
) {
    override val itemRowResource = R.layout.view_item_advice_type

    override fun bind(
        itemView: View,
        item: AdviceTypeResource,
        listener: (AdviceTypeResource, View) -> Unit
    ) {

        val isCurrentlyAdded = getAdviceFromTypeCriteria(item.id, advices) != null

        with(itemView) {
            type_title_tv.text = item.text
            subtype_title_tv.text = item.reason

            if (isCurrentlyAdded) {
                status_tv.visibility = VISIBLE
                subtype_title_tv.visibility = GONE
                checked_iv.visibility = VISIBLE
                advice_type_cl.background =
                    ContextCompat.getDrawable(context, R.drawable.sh_advice_type_configurated)

            } else {
                checked_iv.visibility = GONE
                advice_type_cl.background =
                    ContextCompat.getDrawable(context, R.drawable.sh_new_advice)
                subtype_title_tv.visibility = VISIBLE
                status_tv.visibility = GONE
            }

            setOnClickListener { listener(item, itemView) }
        }
    }
}
