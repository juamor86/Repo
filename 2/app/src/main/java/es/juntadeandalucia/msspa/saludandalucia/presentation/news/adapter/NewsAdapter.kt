package es.juntadeandalucia.msspa.saludandalucia.presentation.news.adapter

import android.view.View
import androidx.core.text.HtmlCompat
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import kotlinx.android.synthetic.main.view_item_new.view.*

class NewsAdapter(listener: (NewsEntity, View) -> Unit) : BaseListAdapter<NewsEntity>(onClickItemListener = listener) {

    override val itemRowResource = R.layout.view_item_new

    override fun bind(itemView: View, item: NewsEntity, listener: (NewsEntity, View) -> Unit) {
        with(itemView) {
            new_title_tv.text = item.title
            new_description_tv.text =
                item.subtitle?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
            Picasso.get().load(if (item.imgHeader?.isEmpty()!!) null else item.imgHeader)
                .placeholder(R.drawable.img_news_placeholder)
                .into(new_header_iv)
            setOnClickListener { listener(item, itemView) }
        }
    }
}
