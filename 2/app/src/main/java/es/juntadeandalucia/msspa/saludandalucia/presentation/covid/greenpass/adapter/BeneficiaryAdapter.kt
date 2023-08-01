package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.view_item_user.view.*

open class BeneficiaryAdapter(
    val context: Context,
    onClickItemListener: (BeneficiaryEntity, View) -> Unit,
    onRemoveItemListener: (BeneficiaryEntity) -> Unit
) : BaseListAdapter<BeneficiaryEntity>(
    onClickItemListener = onClickItemListener,
    onRemoveItemListener = onRemoveItemListener
) {

    override val itemRowResource = R.layout.view_item_user

    override fun bind(
        itemView: View,
        item: BeneficiaryEntity,
        listener: (BeneficiaryEntity, View) -> Unit
    ) {
        with(itemView) {
            if (item.fullName == context.getString(R.string.holder) || item.fullName == context.getString(
                    R.string.recipients
                )
            ) {
                val param = section_title_tv.layoutParams as ViewGroup.MarginLayoutParams

                val marginTop = if (item.fullName == context.getString(R.string.holder)) 0
                else
                    context.resources.getDimensionPixelSize(R.dimen.margin_default_quadruple)

                param.setMargins(
                    0,
                    marginTop,
                    context.resources.getDimensionPixelSize(R.dimen.margin_default_double),
                    context.resources.getDimensionPixelSize(R.dimen.margin_tinny)
                )

                section_title_tv.layoutParams = param

                section_title_tv.text = item.fullName
                section_title_tv.visibility = View.VISIBLE
                user_name_tv.visibility = View.GONE
                user_identifier_tv.visibility = View.GONE
                user_icon_iv.visibility = View.GONE
            } else {
                section_title_tv.visibility = View.GONE
                user_name_tv.visibility = View.VISIBLE
                user_identifier_tv.visibility = View.GONE
                user_icon_iv.visibility = View.VISIBLE
                user_name_tv.text = item.fullName
                user_identifier_tv.text =
                    item.nuhsa

                setOnClickListener { listener(item, itemView) }
            }
        }
    }

    override fun setItemsAndNotify(list: List<BeneficiaryEntity>) {
        val mutable = list.toMutableList()
        mutable.add(0, BeneficiaryEntity("", context.getString(R.string.holder), ""))
        mutable.add(2, BeneficiaryEntity("", context.getString(R.string.recipients), ""))
        super.setItemsAndNotify(mutable)
    }
}
