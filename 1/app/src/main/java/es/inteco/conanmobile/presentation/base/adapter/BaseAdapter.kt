package es.inteco.conanmobile.presentation.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter
 *
 * @param T
 * @property listener
 * @constructor Create empty Base adapter
 */
abstract class BaseAdapter<T>(private val listener: (T) -> Unit) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private var items: List<T> = emptyList()

    abstract val itemRowResource: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                itemRowResource,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    /**
     * Set items and notify
     *
     * @param list
     */
    fun setItemsAndNotify(list: List<T>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(holder.itemView, items[position], listener)
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
