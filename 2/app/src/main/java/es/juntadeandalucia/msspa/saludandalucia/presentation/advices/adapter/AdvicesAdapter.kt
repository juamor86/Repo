package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.adapter

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdvicesStatus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_TYPE_OFF
import kotlinx.android.synthetic.main.view_item_advice_type.view.*

class AdvicesAdapter(
    listener: (AdviceEntity) -> Unit
) : BaseAdapter<AdviceEntity>(listener = listener) {

    override val itemRowResource = R.layout.view_item_advice_type

    override fun bind(
        itemView: View,
        item: AdviceEntity,
        listener: (AdviceEntity) -> Unit
    ) {
        with(itemView) {
            val title =
                item.entry?.get(0)?.extension?.find { it.url == "evento" }?.valueReference?.display
                    ?: item.dataView.title
            type_title_tv.text = title

            if (item.dataView.isOwner) {
                subtype_title_tv.visibility = GONE
                //TODO remove this
                if (false/*item.dataView.isShared*/) {
                    shared_iv.visibility = VISIBLE
                    shared_advice_tv.visibility = VISIBLE
                } else {
                    shared_iv.visibility = GONE
                    shared_advice_tv.visibility = GONE
                }

                checked_iv.visibility = GONE
                advice_type_cl.background = ContextCompat.getDrawable(context, R.drawable.sh_new_advice)

            } else {

                if (item.dataView.sharedBy.isNullOrEmpty()) {
                    subtype_title_tv.visibility = GONE
                } else {
                    subtype_title_tv.text = resources.getString(R.string.new_advice_sent_by).replace("%s", item.dataView.sharedBy!!)
                }

                checked_iv.visibility = VISIBLE
                advice_type_cl.background = ContextCompat.getDrawable(context, R.drawable.sh_advice_type_configurated)

                val entryAdviceEntityReceived: EntryAdviceEntity? = item.dataView.entryAdviceEntityReceived
                when (entryAdviceEntityReceived?.status) {
                    AdvicesStatus.ACTIVE.name.lowercase() -> {
                        checked_iv.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_illustrationscheckmark
                            )
                        )
                        status_tv.text = resources.getString(R.string.avisas_list_advice_accepted)
                        status_tv.visibility = VISIBLE
                    }
                    AdvicesStatus.REQUESTED.name.lowercase() -> {
                        checked_iv.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_warning
                            )
                        )
                        status_tv.text = resources.getString(R.string.avisas_list_advice_pending)
                        status_tv.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.orange_badge
                            )
                        )
                        status_tv.visibility = VISIBLE
                    }
                    ADVICE_TYPE_OFF -> {
                        checked_iv.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_error
                            )
                        )
                        status_tv.text = resources.getString(R.string.avisas_list_advice_refused)
                        status_tv.setTextColor(ContextCompat.getColor(context, R.color.redPink))
                        status_tv.visibility = VISIBLE
                    }
                }
            }

            setOnClickListener { listener(item) }

        }
    }

}
