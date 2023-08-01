package es.inteco.conanmobile.presentation.base.custom.view

import android.content.Context
import android.util.AttributeSet
import android.widget.AdapterView
import es.inteco.conanmobile.domain.entities.KeyValueEntity

/**
 * Custom auto complete text view
 *
 * @constructor Create empty Custom auto complete text view
 */
class CustomAutoCompleteTextView : androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    var clickListener: AdapterView.OnItemClickListener? = null

    var currentItem: KeyValueEntity? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
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

    /**
     * Set item
     *
     * @param item
     */
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

    /**
     * Set item
     *
     * @param itemPosition
     */
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
