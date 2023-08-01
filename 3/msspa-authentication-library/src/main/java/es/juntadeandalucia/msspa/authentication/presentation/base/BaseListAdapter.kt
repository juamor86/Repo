package es.juntadeandalucia.msspa.authentication.presentation.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T>(
    private val onClickItemListener: (T, View) -> Unit,
    private val onRemoveItemListener: ((T) -> Unit) = {},
    diffCallback: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: T, newItem: T) =
            oldItem.toString() == newItem.toString()
    }
) :
    ListAdapter<T, BaseListAdapter.BaseViewHolder>(diffCallback) {

    private lateinit var items: MutableList<T>

    abstract val itemRowResource: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                itemRowResource,
                parent,
                false
            )
        )

    fun setItemsAndNotify(list: List<T>) {
        items = list.toMutableList()
        submitList(items)
    }

    fun deleteItem(position: Int) {
        onRemoveItemListener.invoke(items.removeAt(position))
        notifyItemRemoved(position)
        submitList(items)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(holder.itemView, getItem(position), onClickItemListener)
    }

    abstract fun bind(itemView: View, item: T, listener: (T, View) -> Unit)

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
