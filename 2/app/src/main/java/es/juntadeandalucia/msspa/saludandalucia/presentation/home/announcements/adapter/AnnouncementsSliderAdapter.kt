package es.juntadeandalucia.msspa.saludandalucia.presentation.home.announcements.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.github.islamkhsh.CardSliderAdapter
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AnnouncementEntity

class AnnouncementsSliderAdapter(
    private val context: Context?,
    private val listener: (AnnouncementEntity) -> Unit,
    announcements: ArrayList<AnnouncementEntity>
) :
    CardSliderAdapter<AnnouncementEntity>(announcements) {

    override fun bindView(position: Int, itemContentView: View, item: AnnouncementEntity?) {
        item?.apply {

            Picasso.get().load(item.imgHeader).placeholder(R.drawable.img_news_placeholder)
                .into(itemContentView.findViewById<ImageView>(R.id.announcement_iv))
            itemContentView.setOnClickListener { listener(this) }
        }
    }

    override fun getItemContentLayout(position: Int): Int = R.layout.view_slider_announcement
}
