package es.juntadeandalucia.msspa.authentication.presentation.login.adapter

import android.view.View
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseListAdapter
import kotlinx.android.synthetic.main.msspa_auth_view_item_user.view.*

open class UserAdapter(
    onClickItemListener: (MsspaAuthenticationUserEntity, View) -> Unit,
    onRemoveItemListener: (MsspaAuthenticationUserEntity) -> Unit
) : BaseListAdapter<MsspaAuthenticationUserEntity>(onClickItemListener = onClickItemListener, onRemoveItemListener = onRemoveItemListener) {

    override val itemRowResource = R.layout.msspa_auth_view_item_user

    override fun bind(itemView: View, item: MsspaAuthenticationUserEntity, listener: (MsspaAuthenticationUserEntity, View) -> Unit) {
        with(itemView) {
            user_name_tv.text = item.name
            user_identifier_tv.text =
                if (item.idType.key.isEmpty() || item.idType.key == MsspaAuthenticationUserEntity.ID_TYPE_NUSS_NUHSA) item.nuhsa else item.nuss
            setOnClickListener { listener(item, itemView) }
        }
    }
}
