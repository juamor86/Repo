package es.juntadeandalucia.msspa.saludandalucia.presentation.home.featured.adapter

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.FeaturedEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter

open class FeaturedAdapter(listener: (FeaturedEntity, View) -> Unit) :
    BaseListAdapter<FeaturedEntity>(onClickItemListener = listener) {

    override val itemRowResource = R.layout.view_item_home_featured

    override fun bind(
        itemView: View,
        item: FeaturedEntity,
        listener: (FeaturedEntity, View) -> Unit
    ) {
        with(itemView) {
            Picasso.get().load(item.imgHeader).placeholder(R.drawable.img_news_placeholder)
                .into(itemView.findViewById<ImageView>(R.id.featured_iv))
            setOnClickListener { listener(item, itemView) }
        }
    }
}
