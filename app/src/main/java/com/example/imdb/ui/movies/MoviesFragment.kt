package com.example.imdb.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.databinding.FragmentMoviesBinding
import com.example.imdb.ui.base.MvvmFragment
import com.example.imdb.ui.bottom.BottomFragmentDirections.Companion.actionBottomToMovieDetails
import com.example.imdb.ui.movies.helper.MoviesAdapter
import com.example.imdb.utils.extensions.string
import com.example.imdb.utils.helper.PaginationScrollListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : MvvmFragment<FragmentMoviesBinding, MoviesViewModel>(
    R.layout.fragment_movies,
    MoviesViewModel::class.java
) {
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState)?.apply {
        setHasOptionsMenu(true)
    }

    override fun onEveryInitialization(savedBundle: Bundle?) {
        actionBar?.setTitle(R.string.main_menu_movies)

        with(data) {
            movies.observeNonNull(viewLifecycleOwner, {
                moviesAdapter.updateCustom(it)
            })

            loading.observeNonNull(viewLifecycleOwner, {
                moviesAdapter.updateLoading(it)
            })

            eventError.observeEvent(viewLifecycleOwner, {
                val message = it.message ?: string(R.string.error_message_unknown)
                Snackbar.make(moviesRecycler, message, LENGTH_LONG).show()
            })

            eventClicked.observeEvent(viewLifecycleOwner, {
                mainNavigation?.navigate(actionBottomToMovieDetails(it.imdbId))
            })
        }

        moviesAdapter = MoviesAdapter(data)

        val linearLayoutManager = LinearLayoutManager(activity)

        moviesRecycler?.apply {
            layoutManager = linearLayoutManager
            itemAnimator = null
            setHasFixedSize(true)
            adapter = moviesAdapter
        }

        moviesRecycler?.addOnScrollListener(
            object : PaginationScrollListener(linearLayoutManager) {
                override fun onListBottom() = data.loadMovies()
                override fun isLastPage(): Boolean = data.isLimitReached
                override fun isLoading(): Boolean = data.loading.value
            }
        )
    }

    override fun onFirstInitialization() {
        data.initialize()
    }

    override fun onDestroyView() {
        moviesRecycler.adapter = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)

        val item = menu.findItem(R.id.menu_toolbar_search)
        val view = item.actionView as SearchView

        view.maxWidth = Int.MAX_VALUE

        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                data.search(query)
                view.apply { if (!isIconified) isIconified = true }
                item.collapseActionView()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean = false
        })
    }
}