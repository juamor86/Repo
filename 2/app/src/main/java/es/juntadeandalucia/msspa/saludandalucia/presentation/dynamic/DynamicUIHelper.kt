package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicHomeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicItemEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import kotlinx.android.synthetic.main.item_dyn_button.view.image_iv
import kotlinx.android.synthetic.main.item_dyn_button.view.title_tv
import kotlinx.android.synthetic.main.item_dyn_h_img_t_tx.view.*

class DynamicUIHelper {

    lateinit var context: Context

    companion object {

        fun getHomeViews(
            context: Context,
            parent: ViewGroup,
            homeEntity: DynamicHomeEntity,
            clickListener: (DynamicElementEntity) -> Unit
        ): List<View> {
            val homeList = mutableListOf<View>()
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            for (layout in homeEntity.layouts) {
                val layoutRes = DynamicConsts.UI.getDynamicElem(layout.type)
                if (layoutRes != null) {
                    val layoutView = inflater.inflate(layoutRes, parent, false) as ViewGroup
                    if (layout.title.text.isNotEmpty()) layoutView.findViewById<TextView>(R.id.title_tv).text =
                        layout.title.text
                    for (item in layout.children) {
                        val itemRes = DynamicConsts.UI.getDynamicElem(item.type)
                        itemRes?.let {
                            val view = inflater.inflate(it, layoutView, false)
                            bind(view, item, clickListener)
                            (layoutView as DynamicLayout).addChildren(view)
                        }
                    }
                    homeList.add(layoutView)
                }
            }
            return homeList
        }

        private fun bind(
            view: View,
            elementEntity: DynamicElementEntity,
            clickListener: (DynamicElementEntity) -> Unit
        ) {
            view.setOnClickListener { clickListener.invoke(elementEntity) }
            elementEntity.background.apply {
                if (isNotEmpty()) {
                    view.background = Color.parseColor(this).toDrawable()
                }
            }

            view.image_iv?.apply {
                Picasso.get().load(elementEntity.icon.source).fit().into(this)
            }

            view.title_tv?.apply {
                elementEntity.title.let {
                    text = it.text
                    if (it.color.isNotEmpty()) {
                        setTextColor(Color.parseColor(it.color))
                    }
                    contentDescription = it.alt
                }
            }

            view.text_tv?.apply {
                elementEntity.subtitle.let {
                    text = it.text
                    if (it.color.isNotEmpty()) {
                        setTextColor(Color.parseColor(it.color))
                    }
                }
            }
        }

        fun getFabViews(
            context: Context,
            parent: ViewGroup,
            children: List<DynamicItemEntity>,
            clickListener: (NavigationEntity) -> Unit
        ): List<View> {
            return getFabViewsInternal(
                context,
                parent,
                children,
                R.layout.item_dyn_fab,
                clickListener
            )
        }

        private fun getFabViewsInternal(
            context: Context,
            parent: ViewGroup,
            children: List<DynamicItemEntity>,
            layout: Int,
            clickListener: (NavigationEntity) -> Unit
        ): List<View> {
            val views = mutableListOf<View>()
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            for (item in children) {
                val itemView = inflater.inflate(layout, parent, false)
                bind(itemView, item, clickListener)
                item.children.apply {
                    if (isNotEmpty()) {
                        getFabViewsInternal(
                            context,
                            itemView.findViewById(R.id.children_ll),
                            this,
                            R.layout.item_dyn_fab_child,
                            clickListener
                        )
                    }
                }
                parent.addView(itemView)
            }

            return views
        }

        private fun bind(
            view: View,
            elementEntity: DynamicItemEntity,
            clickListener: (NavigationEntity) -> Unit
        ) {
            view.setOnClickListener { elementEntity.navigation?.let {
                clickListener.invoke(
                    it
                )
            } }

            elementEntity.icon.source.apply {
                if (isNotEmpty() && view.image_iv != null) {
                    Picasso.get().load(this).into(view.image_iv)
                }
            }

            view.findViewById<TextView>(R.id.title_tv)?.apply {
                elementEntity.title?.let {
                    text = it.text
                    contentDescription = it.alt
                }
            }
        }
    }
}
