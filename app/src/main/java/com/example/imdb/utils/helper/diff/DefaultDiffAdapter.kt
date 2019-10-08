package com.example.imdb.utils.helper.diff

import androidx.recyclerview.widget.RecyclerView

abstract class DefaultDiffAdapter<T : DiffItem> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val items = mutableListOf<T>()
    protected lateinit var view: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        view = recyclerView
    }

    override fun getItemCount(): Int = items.size

    fun updateCustom(new: List<T>) {
        if (items.isEmpty()) {
            if (new.isEmpty()) return
            items.addAll(new)
            notifyItemRangeInserted(0, new.size)
            return
        }

        if (new.isEmpty()) {
            val size = items.size
            items.clear()
            notifyItemRangeRemoved(0, size)
            return
        }

        val callback = DefaultDiffCallback(items, new)
        val result = androidx.recyclerview.widget.DiffUtil.calculateDiff(callback)

        items.apply {
            clear()
            addAll(new)
        }

        result.dispatchUpdatesTo(this@DefaultDiffAdapter)
    }

    fun updateChanged(vararg positions: Int): Unit = positions.forEach {
        if (it in 1 until items.size) notifyItemChanged(it)
    }

    fun updateChanged(range: IntRange): Unit = with(range) {
        notifyItemRangeChanged(start, endInclusive - start + 1)
    }
}