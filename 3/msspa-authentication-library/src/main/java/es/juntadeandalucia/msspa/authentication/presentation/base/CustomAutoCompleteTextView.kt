package es.juntadeandalucia.msspa.authentication.presentation.base

import android.content.Context
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity

class CustomAutoCompleteTextView : AutoCompleteTextView {

    var clickListener: AdapterView.OnItemClickListener? = null

    var currentItem: KeyValueEntity? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    init {
        setOnClickListener { showDropDown() }
        super.setOnItemClickListener { parent, _, position, _ ->
            onItemClicks(parent, position)
        }
    }

    fun setItem(item: KeyValueEntity) {
        findPosition(item)?.let { setItem(it) }
    }

    private fun findPosition(item: KeyValueEntity): Int? {
        var position: Int? = null
        for (pos in 0 until adapter.count) {
            if (item == (adapter.getItem(pos))) {
                position = pos
                break
            }
        }
        return position
    }

    fun setItem(itemPosition: Int) {
        currentItem = adapter.getItem(itemPosition) as KeyValueEntity
        setText(currentItem.toString(), false)
        clickListener?.onItemClick(
                null,
                adapter.getView(itemPosition, null, null),
                itemPosition, adapter.getItemId(itemPosition)
        )
    }

    override fun setOnItemClickListener(l: AdapterView.OnItemClickListener?) {
        this.clickListener = l
    }

    private fun onItemClicks(
        adapter: AdapterView<*>,
        position: Int
    ) {
        setItem(adapter.getItemAtPosition(position) as KeyValueEntity)
    }
}
