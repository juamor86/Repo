package es.juntadeandalucia.msspa.saludandalucia.presentation.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicItemEntity
import kotlinx.android.synthetic.main.view_menu_group_item.view.*

class MenuAdapter(
    private val context: Context,
    items: List<DynamicItemEntity>
) :
    BaseExpandableListAdapter() {

    private val menuItems: MutableList<DynamicItemEntity> = mutableListOf()
    private val greenColor = ContextCompat.getColor(context, R.color.primary)
    private val blackColor = ContextCompat.getColor(context, R.color.black)

    init {
        processItems(items)
    }

    private fun processItems(items: List<DynamicItemEntity>) {

        var aux: DynamicItemEntity
        var aux2: DynamicItemEntity
        for (item in items) {
            aux = item.copy()
            aux.children = mutableListOf()
            menuItems.add(aux)
            var i = 1
            for (item2 in item.children) {
                aux2 = item2.copy()
                aux.children.add(aux2)
                if (item2.children.isNotEmpty()) {
                    aux2.isParent = true
                    aux.children.addAll(i, item2.children)
                    i += item2.children.size + 1
                } else {
                    i++
                }
            }
        }
    }

    private val layoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getGroup(groupPosition: Int) = menuItems[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int) =
        menuItems[groupPosition].children[childPosition]

    override fun getGroupCount() = menuItems.size

    override fun getChildrenCount(groupPosition: Int) = menuItems[groupPosition].children.size

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) =
        !getChild(groupPosition, childPosition).isParent

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView_: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView_
        val item = getGroup(groupPosition)
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_menu_group_item, null)
        }
        with(convertView!!) {
            item_text_tv.text = item.toString()

            if (isSelected) {
                item_text_tv.setTextColor(greenColor)
                item_icon_iv.setColorFilter(
                    greenColor,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                item_text_tv.setTextColor(blackColor)
                item_icon_iv.setColorFilter(
                    blackColor,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            if (item.children.isNotEmpty()) {
                if (isExpanded) {
                    item_text_tv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary
                        )
                    )
                    group_cursor.setColorFilter(greenColor)
                    background =
                        ContextCompat.getDrawable(context, R.color.jungle_green_10)
                    group_cursor.rotation = 90f
                } else {
                    item_icon_iv.setColorFilter(
                        blackColor,
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    item_text_tv.setTextColor(blackColor)
                    group_cursor.setColorFilter(blackColor)
                    background =
                        ContextCompat.getDrawable(context, android.R.color.transparent)
                    group_cursor.rotation = 0f
                }
            } else {
                group_cursor.visibility = View.GONE
            }
            if (item.icon.source.isNotBlank()) {
                Picasso.get().load(item.icon.source).into(item_icon_iv)
            }
            return this
        }
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        if (convertView == null) {
            val item = getChild(groupPosition, childPosition)
            if (item.isParent) {
                convertView = layoutInflater.inflate(R.layout.view_menu_parent_item, null)
            } else {
                convertView = layoutInflater.inflate(R.layout.view_menu_item, null)
            }
            convertView!!.item_text_tv.text = item.toString()
        }
        return convertView
    }

    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int) =
        (groupPosition * 10 + childPosition).toLong()
}
