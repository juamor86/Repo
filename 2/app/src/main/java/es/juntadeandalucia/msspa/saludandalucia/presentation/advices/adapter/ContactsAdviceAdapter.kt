package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.adapter

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseAdapter
import kotlinx.android.synthetic.main.view_item_import_contact.view.*
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.getLabelContact

class ContactsAdviceAdapter(
    listener: (ContactAdviceEntity) -> Unit
) : BaseAdapter<ContactAdviceEntity>(listener = listener) {

    override val itemRowResource = R.layout.view_item_import_contact

    override fun bind(
        itemView: View,
        item: ContactAdviceEntity,
        listener: (ContactAdviceEntity) -> Unit
    ) {
        with(itemView) {
            contact_advice_tv.text = item.number
            type_contact_advice_tv.text = getLabelContact(resources, item)
            setOnClickListener { listener(item) }
        }
    }
}
