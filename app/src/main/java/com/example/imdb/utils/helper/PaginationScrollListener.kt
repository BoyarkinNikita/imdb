package com.example.imdb.utils.helper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener constructor(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {
    private val bottomVisibleElement = 3

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)

        val itemCount = layoutManager.childCount
        val position = layoutManager.itemCount - bottomVisibleElement
        val visiblePosition = layoutManager.findFirstVisibleItemPosition()

        if (isLoading() || isLastPage()) return
        if (itemCount + visiblePosition >= position && visiblePosition >= 0) onListBottom()
    }

    protected abstract fun onListBottom()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}