package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.adapter

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.view_item_user.view.*

open class UserAdapter(
    onClickItemListener: (QuizUserEntity, View) -> Unit,
    onRemoveItemListener: (QuizUserEntity) -> Unit
) : BaseListAdapter<QuizUserEntity>(onClickItemListener = onClickItemListener, onRemoveItemListener = onRemoveItemListener) {

    override val itemRowResource = R.layout.view_item_user

    override fun bind(itemView: View, item: QuizUserEntity, listener: (QuizUserEntity, View) -> Unit) {
        with(itemView) {
            user_name_tv.text = item.name
            user_identifier_tv.text =
                if (item.idType.key.isEmpty() || item.idType.key == QuizUserEntity.ID_TYPE_NUHSA) item.nuhsa else item.identification
            setOnClickListener { listener(item, itemView) }
        }
    }
}
