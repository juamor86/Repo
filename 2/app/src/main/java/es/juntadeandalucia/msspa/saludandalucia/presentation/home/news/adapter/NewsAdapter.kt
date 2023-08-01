package es.juntadeandalucia.msspa.saludandalucia.presentation.home.news.adapter

import android.view.View
import androidx.core.text.HtmlCompat
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import kotlin.math.min
import kotlinx.android.synthetic.main.view_item_home_new.view.*

open class NewsAdapter(listener: (NewsEntity, View) -> Unit) :
    BaseListAdapter<NewsEntity>(onClickItemListener = listener) {

    companion object {
        internal const val MAX_NEWS = 5
    }

    override val itemRowResource = R.layout.view_item_home_new

    override fun getItemCount() = min(super.getItemCount(), MAX_NEWS)

    override fun bind(itemView: View, item: NewsEntity, listener: (NewsEntity, View) -> Unit) {
        with(itemView) {
            new_home_title_tv.text = item.title
            new_home_subtitle_tv.text =
                item.subtitle?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
            Picasso.get().load(if (item.imgHeader?.isEmpty()!!) null else item.imgHeader)
                .placeholder(R.drawable.img_news_placeholder)
                .into(new_home_header_iv)

            setOnClickListener { listener(item, itemView) }
        }
    }
}
