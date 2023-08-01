package es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

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

    fun getSelectedItem(): T? {
        if (selectedPosition != -1) return getItem(selectedPosition) else return null
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItemAt(position: Int): T {
        return getItem(position)
    }

    fun setOnSelectedItemListener(listener: AdapterView.OnItemSelectedListener) {
        selectListener = listener
    }

    abstract fun bind(itemView: View, item: T)

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
