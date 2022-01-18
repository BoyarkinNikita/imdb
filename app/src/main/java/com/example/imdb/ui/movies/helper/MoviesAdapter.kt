package com.example.imdb.ui.movies.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.databinding.ItemMovieBinding
import com.example.imdb.databinding.ItemProgressBinding
import com.example.imdb.di.GlideApp
import com.example.imdb.di.get
import com.example.imdb.ui.movies.MoviesViewModel
import com.example.imdb.utils.diff.DefaultDiffAdapter
import com.example.imdb.utils.diff.DiffItem

class MoviesAdapter(
    private val data: MoviesViewModel
) : DefaultDiffAdapter<DiffItem>() {
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
            R.layout.item_movie -> ItemMoviesViewHolder(
                binding as ItemMovieBinding
            )
            R.layout.item_progress -> ItemProgressViewHolder(
                binding as ItemProgressBinding
            )
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder.itemViewType) {
            R.layout.item_movie ->
                (holder as ItemMoviesViewHolder).bind(data, item as MovieItem)

            R.layout.item_progress ->
                (holder as ItemProgressViewHolder).bind()

            else -> throw IllegalStateException()
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is ItemMoviesViewHolder) holder.cleanup()
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
            data: MoviesViewModel,
            item: MovieItem
        ) {
            binding.also {
                it.data = data
                it.item = item
                it.executePendingBindings()
            }
        }

        fun cleanup() {
            GlideApp.with(get<Context>())
                .clear(binding.itemMoviePoster)
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