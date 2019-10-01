package com.example.imdb.ui.main.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.utils.helper.FlowFragment
import com.example.imdb.utils.helper.PaginationScrollListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : FlowFragment() {
    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)

        viewModel.apply {
            movies.observe(this@MoviesFragment, Observer {
                moviesAdapter.updateAll(it)
            })

            isLoading.observe(this@MoviesFragment, Observer {
                moviesAdapter.updateLoading(it)
            })

            error.observe(this@MoviesFragment, Observer {
                Snackbar.make(moviesRecycler, it.message ?: "Unknown error.", LENGTH_LONG).show()
            })
        }

        moviesAdapter = MoviesAdapter(viewModel)
        val linearLayoutManager = LinearLayoutManager(context)

        moviesRecycler.apply {
            layoutManager = linearLayoutManager
            itemAnimator = null
            isNestedScrollingEnabled = true

            setHasFixedSize(true)

            adapter = moviesAdapter
        }

        moviesRecycler.addOnScrollListener(
            object : PaginationScrollListener(linearLayoutManager) {
                override fun onListBottom() = viewModel.loadMovies()
                override fun isLastPage(): Boolean = viewModel.isLimitReached
                override fun isLoading(): Boolean = viewModel.isLoading.value ?: false
            }
        )

        viewModel.initialize()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity?)?.supportActionBar?.setTitle(R.string.main_menu_movies)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)

        val item = menu.findItem(R.id.menu_toolbar_search)
        val view = item.actionView as SearchView

        view.maxWidth = Int.MAX_VALUE

        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                view.apply { if (!isIconified) isIconified = true }
                item.collapseActionView()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean = false
        })
    }
}