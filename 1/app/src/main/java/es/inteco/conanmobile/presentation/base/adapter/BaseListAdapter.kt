package es.inteco.conanmobile.presentation.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Base list adapter
 *
 * @param T
 * @property onClickItemListener
 * @property onRemoveItemListener
 * @constructor
 *
 * @param diffCallback
 */
abstract class BaseListAdapter<T>(
    private val onClickItemListener: ((T) -> Unit) = {},
    private val onRemoveItemListener: ((T) -> Unit) = {},
    diffCallback: ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: T, newItem: T) =
            oldItem.toString() == newItem.toString()
    }
) : ListAdapter<T, BaseListAdapter.BaseViewHolder>(diffCallback) {

    private lateinit var items: MutableList<T>

    abstract val itemRowResource: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder(
        LayoutInflater.from(parent.context).inflate(
            itemRowResource, parent, false
        )
    )

    /**
     * Set items and notify
     *
     * @param list
     */
    open fun setItemsAndNotify(list: List<T>) {
        items = list.toMutableList()
        submitList(items)
    }

    /**
     * Delete item
     *
     * @param position
     */
    fun deleteItem(position: Int) {
        onRemoveItemListener.invoke(items.removeAt(position))
        notifyItemRemoved(position)
        submitList(items)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(holder.itemView, getItem(position), onClickItemListener)
    }

    /**
     * Bind
     *
     * @param itemView
     * @param item
     * @param listener
     * @receiver
     */
    abstract fun bind(itemView: View, item: T, listener: (T) -> Unit)

    /**
     * Base view holder
     *
     * @constructor
     *
     * @param itemView
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
