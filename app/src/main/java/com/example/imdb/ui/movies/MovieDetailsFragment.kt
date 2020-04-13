package com.example.imdb.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.imdb.R
import com.example.imdb.databinding.FragmentMovieDetailsBinding
import com.example.imdb.ui.base.MvvmFragment
import com.example.imdb.ui.main.MainFragmentDirections
import com.example.imdb.ui.main.MainFragmentDirections.Companion.actionMainToPreview
import com.example.imdb.utils.extensions.setOnClickWithDoubleCheck
import com.example.imdb.utils.field.NullableObserver
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment : MvvmFragment<FragmentMovieDetailsBinding, MovieDetailsViewModel>(
    R.layout.fragment_movie_details,
    MovieDetailsViewModel::class.java
) {
    private val args by navArgs<MovieDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState)?.apply {
        setHasOptionsMenu(true)
    }

    override fun onEveryInitialization(savedBundle: Bundle?) {
        actionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)

            title = data.details.value?.title ?: ""
        }

        data.details.observeNullable(viewLifecycleOwner, NullableObserver {
            actionBar?.title = it?.title ?: return@NullableObserver
        })

        movieDetailsPoster?.setOnClickWithDoubleCheck {
            val uri = data.details.value?.posterUrl ?: return@setOnClickWithDoubleCheck
            applicationNavigation?.navigate(actionMainToPreview(uri))
        }
    }

    override fun onFirstInitialization() {
        with(data) {
            imdbId = args.imdbId
            loadDetails()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            mainNavigation?.popBackStack()
            true
        }

        else -> false
    }
}