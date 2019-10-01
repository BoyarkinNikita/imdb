package com.example.imdb.ui.main.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.databinding.ItemMovieBinding
import com.example.imdb.databinding.ItemProgressBinding

class MoviesAdapter(
    private val viewModel: MoviesViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<Any>()

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is MovieItem -> R.layout.item_movie
        is ProgressItem -> R.layout.item_progress
        else -> throw IllegalStateException()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, viewType,
            parent, false
        )

        return when (viewType) {
            R.layout.item_movie -> ItemMoviesViewHolder(binding as ItemMovieBinding)
            R.layout.item_progress -> ItemProgressViewHolder(binding as ItemProgressBinding)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder.itemViewType) {
            R.layout.item_movie ->
                (holder as ItemMoviesViewHolder).bind(viewModel, item as MovieItem)

            R.layout.item_progress ->
                (holder as ItemProgressViewHolder).bind()

            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount() = items.size

    fun updateAll(data: Iterable<Any>) {
        items.apply {
            clear()
            addAll(data)
        }

        notifyDataSetChanged()
    }

    fun updateLoading(isLoading: Boolean) {
        if (isLoading) {
            if (items.find { it is ProgressItem } != null) return
            items.add(ProgressItem())
            notifyItemInserted(items.lastIndex)
        } else {
            val index = items.indexOfFirst { it is ProgressItem }.also { if (it < 0) return }
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class ItemMoviesViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            viewModel: MoviesViewModel,
            item: MovieItem
        ) {
            binding.also {
                it.viewModel = viewModel
                it.item = item
                it.executePendingBindings()
            }
        }
    }

    class ItemProgressViewHolder(
        private val binding: ItemProgressBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.executePendingBindings()
        }
    }
}