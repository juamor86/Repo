package es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.adapter

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdvicesStatus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceExtensionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.adapter.AdvicesAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_TYPE_OFF
import kotlinx.android.synthetic.main.view_item_advice_contact.view.*
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

class BaseContactsAdapter(
    onClickItemListener: KFunction2<ValueReferenceEntity, View, Unit>,
    onRemoveItemListener: KFunction1<ValueReferenceEntity, Unit>,
    val removeButtonListener: KFunction2<Context, ValueReferenceEntity, Unit>
) : BaseListAdapter<ValueReferenceEntity>(
    onClickItemListener = onClickItemListener,
    onRemoveItemListener = onRemoveItemListener
) {
    override val itemRowResource = R.layout.view_item_advice_contact

    private var STATUS = "status"
    var position: Int = 0

    override fun bind(itemView: View, item: ValueReferenceEntity, listener: (ValueReferenceEntity, View) -> Unit) {
        with(itemView) {
            if (position == 0) {
                separator_v.visibility = View.GONE
            }

            user_tv.text = item.name ?: item.display
            status_tv.text = resources.getString(R.string.avisas_list_advice_pending)

            when (getStatus(item.extension)) {
                AdvicesStatus.ACTIVE.name.lowercase() -> {
                    status_tv.text = resources.getString(R.string.avisas_list_advice_accepted)
                    status_tv.setTextColor(ContextCompat.getColor(context, R.color.primary))
                }
                ADVICE_TYPE_OFF -> {
                    status_tv.text = resources.getString(R.string.avisas_list_advice_refused)
                    status_tv.setTextColor(ContextCompat.getColor(context, R.color.redPink))
                }
                else -> {
                    status_tv.text = resources.getString(R.string.avisas_list_advice_pending)
                    status_tv.setTextColor(ContextCompat.getColor(context, R.color.orange_badge))
                }
            }

            delete_iv.setOnClickListener {
                removeButtonListener(context, item)
            }

            setOnClickListener { listener(item, itemView) }

            position++
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        position--
    }

    private fun getStatus(extension: List<ValueReferenceExtensionEntity>?): String {
        var status = ""

        extension?.forEach { ext ->
            if (ext.url == STATUS) {
                status = ext.valueCode
            }
        }

        return status
    }
}