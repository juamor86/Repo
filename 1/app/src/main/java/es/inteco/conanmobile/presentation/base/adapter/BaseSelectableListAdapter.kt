package es.inteco.conanmobile.presentation.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Base selectable list adapter
 *
 * @param T
 * @constructor
 *
 * @param diffCallback
 */
abstract class BaseSelectableListAdapter<T>(
    diffCallback: ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: T, newItem: T) =
            oldItem.toString() == newItem.toString()
    }
) : ListAdapter<T, BaseSelectableListAdapter.BaseViewHolder>(diffCallback) {

    var selectedPosition: Int = -1
    abstract val itemRowResource: Int
    lateinit var items: MutableList<T>
    lateinit var selectListener: AdapterView.OnItemSelectedListener

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
    fun setItemsAndNotify(list: MutableList<T>) {
        items = list
        submitList(list)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(holder.itemView, getItem(position))
        holder.itemView.isSelected = position == selectedPosition

        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
            } else {
                selectedPosition = -1
            }
            notifyDataSetChanged()
            if (selectListener != null) {
                if (selectedPosition > -1) selectListener.onItemSelected(
                    null, holder.itemView, position, getItemId(position)
                ) else selectListener.onNothingSelected(null)
            }
        }
    }

    /**
     * Get selected item
     *
     * @return
     */
    fun getSelectedItem(): T? {
        if (selectedPosition != -1) return getItem(selectedPosition) else return null
    }

    /**
     * Remove at
     *
     * @param position
     */
    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Get item at
     *
     * @param position
     * @return
     */
    fun getItemAt(position: Int): T {
        return getItem(position)
    }

    /**
     * Set on selected item listener
     *
     * @param listener
     */
    fun setOnSelectedItemListener(listener: AdapterView.OnItemSelectedListener) {
        selectListener = listener
    }

    /**
     * Bind
     *
     * @param itemView
     * @param item
     */
    abstract fun bind(itemView: View, item: T)

    /**
     * Base view holder
     *
     * @constructor
     *
     * @param itemView
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
